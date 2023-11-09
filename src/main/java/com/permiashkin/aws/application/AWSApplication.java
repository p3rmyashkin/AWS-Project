package com.permiashkin.aws.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AWSApplication {

    public static void main(String[] args) {
        SpringApplication.run(AWSApplication.class, args);
    }

}
