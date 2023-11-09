package com.permiashkin.aws.application.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContentFile {
    String filename;
    int contentLength;
    byte[] content;
}
