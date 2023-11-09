package com.permiashkin.aws.application.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.permiashkin.aws.application.consts.AwsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class S3Configuration {

    @Bean
    public AmazonS3 amazonS3(AwsConstants awsConstants,
                             AWSStaticCredentialsProvider credentials) {
        if (StringUtils.hasText(awsConstants.getS3Endpoint())) {
            AwsClientBuilder.EndpointConfiguration configuration =
                    new AwsClientBuilder.EndpointConfiguration(
                            awsConstants.getS3Endpoint(),
                            awsConstants.getRegion()
                    );
            return AmazonS3ClientBuilder
                    .standard()
                    .withEndpointConfiguration(configuration)
                    .withCredentials(credentials)
                    .build();
        }
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials)
                .withRegion(Regions.fromName(awsConstants.getRegion()))
                .build();
    }

    @Bean
    protected AWSStaticCredentialsProvider awsCredentialsProvider(AwsConstants awsConstants) {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        awsConstants.getS3AccessKey(),
                        awsConstants.getS3SecretKey()
                )
        );
    }
}
