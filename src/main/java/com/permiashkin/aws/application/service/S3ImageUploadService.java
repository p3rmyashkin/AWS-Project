package com.permiashkin.aws.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.data.ImageMetadata;
import com.permiashkin.aws.application.model.ContentFile;
import com.permiashkin.aws.application.model.S3ObjectClosable;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@AllArgsConstructor
public class S3ImageUploadService {

    private final AwsConstants awsConstants;
    private final AmazonS3 amazonS3;
    private final ImageMetadataService metadataService;
    private final SQSService sqsService;

    public void uploadImage(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        amazonS3.putObject(
                awsConstants.getBucketName(),
                resolveCompleteFilename(file.getOriginalFilename()),
                getFileInputStream(file),
                objectMetadata
        );
        ImageMetadata metadata = metadataService.saveMetadata(file.getOriginalFilename(), file.getSize());
        sqsService.sendMessage(metadata);
    }

    public ContentFile getImageByName(String fileName) {
        try {
            String filepath = resolveCompleteFilename(fileName);
            String bucketName = awsConstants.getBucketName();
            if (amazonS3.doesObjectExist(bucketName, filepath)) {
                S3Object fileObject = amazonS3.getObject(bucketName, filepath);
                try (S3ObjectClosable s3ObjectClosable = new S3ObjectClosable(fileObject)) {
                    byte[] content = s3ObjectClosable.read();
                    return ContentFile.builder()
                            .filename(fileName)
                            .content(content)
                            .contentLength(content.length)
                            .build();
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            log.info("Unable to find image '{}' in the bucket '{}'", fileName, bucketName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AmazonS3Exception e) {
            log.warn("AmazonS3 exception occurred", e);
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()));
        }
    }

    public void deleteFileByName(String fileName) {
        try {
            String filepath = resolveCompleteFilename(fileName);
            String bucketName = awsConstants.getBucketName();
            if (amazonS3.doesObjectExist(bucketName, filepath)) {
                amazonS3.deleteObject(bucketName, filepath);
                metadataService.deleteMetadata(fileName);
            }
        } catch (AmazonS3Exception e) {
            log.warn("AmazonS3 exception occurred", e);
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()));
        }
    }

    @PostConstruct
    void createBucket() {
        String bucketName = awsConstants.getBucketName();
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
    }

    private String resolveCompleteFilename(String filename) {
        return awsConstants.getFolderName() + "/" + filename;
    }

    private InputStream getFileInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            log.error("Error occurred while getting file's input stream", e);
            throw new RuntimeException(e);
        }
    }
}
