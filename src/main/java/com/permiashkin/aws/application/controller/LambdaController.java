package com.permiashkin.aws.application.controller;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.permiashkin.aws.application.consts.AwsConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(AwsConstants.LAMBDA_ENDPOINT)
public class LambdaController {

    private final AWSLambda awsLambda;
    private final AwsConstants awsConstants;

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerLambda() {
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(awsConstants.getLambdaName())
                .withPayload("{\"detail-type\": \"Microservice endpoint\"}");
        awsLambda.invoke(invokeRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
