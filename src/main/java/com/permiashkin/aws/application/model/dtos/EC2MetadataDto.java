package com.permiashkin.aws.application.model.dtos;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EC2MetadataDto {
    String availabilityZone;
    String instanceRegion;
}
