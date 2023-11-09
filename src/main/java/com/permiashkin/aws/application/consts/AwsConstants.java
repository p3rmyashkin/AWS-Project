package com.permiashkin.aws.application.consts;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AwsConstants {

    public static final String AWS_ENDPOINT = "/aws/api/v1";
    public static final String IMAGE_ENDPOINT = AWS_ENDPOINT + "/image";
    public static final String SNS_ENDPOINT = AWS_ENDPOINT + "/sns";
    public static final String LAMBDA_ENDPOINT = AWS_ENDPOINT + "/lambda";

    private final String S3AccessKey;
    private final String S3SecretKey;
    private final String adminAccessKey;
    private final String adminSecretKey;
    private final String EC2AccessKey;
    private final String EC2SecretKey;
    private final String S3Endpoint;
    private final String region;
    private final String bucketName;
    private final String folderName;
    private final String topicArn;
    private final String queueUrl;
    private final String lambdaName;

    public AwsConstants(@Value("${aws.s3.endpoint}") String S3Endpoint,
                        @Value("${aws.s3.access-key}") String S3AccessKey,
                        @Value("${aws.s3.secret-key}") String S3SecretKey,
                        @Value("${aws.s3.bucket-name}") String bucketName,
                        @Value("${aws.s3.folder}") String folderName,
                        @Value("${aws.ec2.access-key}") String EC2AccessKey,
                        @Value("${aws.ec2.secret-key}") String EC2SecretKey,
                        @Value("${aws.admin.access-key}") String adminAccessKey,
                        @Value("${aws.admin.secret-key}") String adminSecretKey,
                        @Value("${aws.sns.topic-arn}") String topicArn,
                        @Value("${aws.sqs.queue-url}") String queueUrl,
                        @Value("${aws.region}") String region,
                        @Value("${aws.lambda.function-name}") String functionName
    ) {
        this.S3AccessKey = S3AccessKey;
        this.S3SecretKey = S3SecretKey;
        this.adminAccessKey = adminAccessKey;
        this.adminSecretKey = adminSecretKey;
        this.EC2AccessKey = EC2AccessKey;
        this.EC2SecretKey = EC2SecretKey;
        this.S3Endpoint = S3Endpoint;
        this.region = region;
        this.bucketName = bucketName;
        this.folderName = folderName;
        this.topicArn = topicArn;
        this.queueUrl = queueUrl;
        this.lambdaName = functionName;
    }
}
