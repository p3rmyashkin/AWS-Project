package com.permiashkin.aws.application.model;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class File {
    String name;
    String extension;
}
