package com.permiashkin.aws.application.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.data.ImageMetadata;
import com.permiashkin.aws.application.model.MessageBody;
import com.permiashkin.aws.application.providers.ImageDownloadUrlProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SQSService {

    private final AwsConstants awsConstants;
    private final AmazonSQS amazonSQS;
    private final ImageDownloadUrlProvider downloadUrlProvider;

    public void sendMessage(ImageMetadata imageMetadata) {
        String messageBody = MessageBody.builder()
                .name(imageMetadata.getName())
                .fileExtension(imageMetadata.getFileExtension())
                .size(imageMetadata.getSize())
                .lastUpdated(imageMetadata.getLastUpdated())
                .downloadUrl(downloadUrlProvider.getDownloadUrlForImage(imageMetadata))
                .build()
                .toString();
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(awsConstants.getQueueUrl())
                .withMessageBody(messageBody);
        amazonSQS.sendMessage(sendMessageRequest);
        log.info("Message was send\n{}", messageBody);
    }

    public List<Message> readAllMessages() {
        String queueUrl = awsConstants.getQueueUrl();
        List<Message> messages = amazonSQS.receiveMessage(queueUrl).getMessages();
        log.info("Number of messages read from queue '{}': '{}'", queueUrl, messages.size());
        return messages;
    }

    public void deleteMessages(List<Message> messages) {
        for (Message message : messages) {
            amazonSQS.deleteMessage(awsConstants.getQueueUrl(), message.getReceiptHandle());
        }
    }
}
