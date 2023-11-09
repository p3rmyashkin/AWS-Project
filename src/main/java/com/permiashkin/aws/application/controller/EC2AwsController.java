package com.permiashkin.aws.application.controller;

import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.model.dtos.EC2MetadataDto;
import com.permiashkin.aws.application.providers.EC2MetadataProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(AwsConstants.AWS_ENDPOINT)
public class EC2AwsController {

    private final EC2MetadataProvider ec2MetadataProvider;

    @GetMapping("/metadata")
    public ResponseEntity<EC2MetadataDto> getEC2Metadata() {
        return ResponseEntity.ok(ec2MetadataProvider.provideMetadata());
    }
}
