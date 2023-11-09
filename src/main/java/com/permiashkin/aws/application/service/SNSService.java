package com.permiashkin.aws.application.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishBatchRequest;
import com.amazonaws.services.sns.model.PublishBatchRequestEntry;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sqs.model.Message;
import com.permiashkin.aws.application.consts.AwsConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class SNSService {

    private static final String EMAIL_PROTOCOL = "email";
    private static final String SUBJECT_VALUE = "New images have been uploaded to S3 Storage";


    private final AwsConstants awsConstants;
    private final SQSService sqsService;
    private final AmazonSNS amazonSNS;
    private final SubscriptionService subscriptionService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void readMessagesFromQueueAndNotify() {
        List<Message> messages = sqsService.readAllMessages();
        if (messages.size() != 0) {
            notify(messages);
            sqsService.deleteMessages(messages);
        }
    }

    private void notify(List<Message> messages) {
        List<PublishBatchRequestEntry> batchRequestEntries = messages
                .stream()
                .map(this::createPublishRequestEntry)
                .toList();
        PublishBatchRequest batchRequest = new PublishBatchRequest()
                .withTopicArn(awsConstants.getTopicArn())
                .withPublishBatchRequestEntries(batchRequestEntries);
        amazonSNS.publishBatch(batchRequest);
        log.info("Batch of messages {} was published to SNS service", batchRequestEntries.size());
    }

    public void subscribeEmail(String email) {
        String subscriptionArn = sendSubscribeEvent(email);
        subscriptionService.save(email, subscriptionArn);
        log.info("Customer '{}' has been successfully subscribed to topic '{}'",
                email,
                awsConstants.getTopicArn()
        );
    }

    public void unsubscribeEmail(String email) {
        String subscriptionArn = subscriptionService.findSubscriptionArnByEmail(email);
        sendUnsubscribeEvent(subscriptionArn);
        subscriptionService.deleteByEmail(email);
        log.info("Customer '{}' has been successfully unsubscribed from topic '{}'",
                email,
                awsConstants.getTopicArn()
        );
    }


    private PublishBatchRequestEntry createPublishRequestEntry(Message message) {
        return new PublishBatchRequestEntry()
                .withId(message.getMessageId())
                .withMessage(message.getBody())
                .withSubject(SUBJECT_VALUE);
    }

    private void sendUnsubscribeEvent(String subscriptionArn) {
        try {
            amazonSNS.unsubscribe(new UnsubscribeRequest(subscriptionArn));
        } catch (Exception e) {
            log.info(
                    "Unable to unsubscribe subscriptionArn '{}' from the topic '{}'",
                    subscriptionArn,
                    awsConstants.getTopicArn()
            );
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private String sendSubscribeEvent(String email) {
        try {
            SubscribeRequest request = new SubscribeRequest()
                    .withTopicArn(awsConstants.getTopicArn())
                    .withEndpoint(email)
                    .withReturnSubscriptionArn(true)
                    .withProtocol(EMAIL_PROTOCOL);
            return amazonSNS.subscribe(request)
                    .getSubscriptionArn();
        } catch (Exception e) {
            log.error("Unable to subscribe email '{}' for the topic '{}'", email, awsConstants.getTopicArn());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
