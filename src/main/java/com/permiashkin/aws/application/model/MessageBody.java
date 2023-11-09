package com.permiashkin.aws.application.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MessageBody {

    private static final String MESSAGE_TEMPLATE = """
            New image has been added to S3 AWS Bucket.
            Image metadata:
                name: %s,
                extension: %s,
                size: %d,
                lastUpdated: %s.
            File can be downloaded by following url: %s
            """;


    private String name;
    private String fileExtension;
    private long size;
    private Date lastUpdated;
    private String downloadUrl;

    @Override
    public String toString() {
        return String.format(
                MESSAGE_TEMPLATE,
                name, fileExtension, size, lastUpdated.toString(), downloadUrl
        );
    }
}

