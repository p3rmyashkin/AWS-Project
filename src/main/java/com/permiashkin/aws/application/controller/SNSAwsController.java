package com.permiashkin.aws.application.controller;

import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.data.Subscription;
import com.permiashkin.aws.application.model.dtos.EmailDto;
import com.permiashkin.aws.application.repository.SubscriptionRepository;
import com.permiashkin.aws.application.service.SNSService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(AwsConstants.SNS_ENDPOINT)
public class SNSAwsController {

    private final SNSService snsService;
    private final SubscriptionRepository subscriptionRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody EmailDto emailDto) {
        snsService.subscribeEmail(emailDto.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody EmailDto emailDto) {
        snsService.unsubscribeEmail(emailDto.getEmail());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/mapping")
    public ResponseEntity<List<Subscription>> subscriptionsMapping() {
        return ResponseEntity
                .ok(subscriptionRepository.findAll());
    }
}
