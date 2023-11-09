package com.permiashkin.aws.application.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.permiashkin.aws.application.consts.AwsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SNSConfiguration {

    @Bean
    public AmazonSNS amazonSNS(AwsConstants awsConstants) {
        AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsConstants.getAdminAccessKey(), awsConstants.getAdminSecretKey())
        );
        return AmazonSNSClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Regions.fromName(awsConstants.getRegion()))
                .build();
    }
}
