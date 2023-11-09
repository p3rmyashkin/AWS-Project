package com.permiashkin.aws.application.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.permiashkin.aws.application.consts.AwsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EC2Configuration {

    @Bean
    public AmazonEC2 amazonEC2(AwsConstants awsConstants) {
        AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsConstants.getEC2AccessKey(), awsConstants.getEC2SecretKey())
        );
        return AmazonEC2ClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Regions.fromName(awsConstants.getRegion()))
                .build();
    }
}
