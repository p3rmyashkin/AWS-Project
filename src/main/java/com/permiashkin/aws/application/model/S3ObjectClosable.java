package com.permiashkin.aws.application.model;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class S3ObjectClosable implements Closeable {

    private final S3Object s3Object;

    public S3ObjectInputStream getObjectContent() {
        return s3Object.getObjectContent();
    }

    public byte[] read() {
        try {
            return getObjectContent().readAllBytes();
        } catch (IOException e) {
            log.error("Error occurred while reading object content", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public void close() throws IOException {
        s3Object.getObjectContent().abort();
        s3Object.close();
    }
}
