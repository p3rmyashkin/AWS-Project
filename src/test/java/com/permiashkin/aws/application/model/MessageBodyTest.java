package com.permiashkin.aws.application.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class MessageBodyTest {

    private static final String EXPECTED_MESSAGE_OUTPUT = """
            New image has been added to S3 AWS Bucket.
            Image metadata:
                name: name,
                extension: extension,
                size: 1,
                lastUpdated: Thu Jan 01 01:00:00 CET 1970.
            File can be downloaded by following url: downloadUrl
            """;

    @Test
    void givenMessage_thenToString_thenMessageIsValid() {
        // GIVEN
        MessageBody messageBody = MessageBody.builder()
                .name("name")
                .fileExtension("extension")
                .lastUpdated(new Date(1))
                .downloadUrl("downloadUrl")
                .size(1)
                .build();

        // WHEN
        String message = messageBody.toString();

        // THEN
        assertThat(message).isEqualTo(EXPECTED_MESSAGE_OUTPUT);
    }
}
