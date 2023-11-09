package com.permiashkin.aws.application.controller;

import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.service.ImageMetadataService;
import com.permiashkin.aws.application.model.ContentFile;
import com.permiashkin.aws.application.data.ImageMetadata;
import com.permiashkin.aws.application.service.S3ImageUploadService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;


@RestController
@AllArgsConstructor
@RequestMapping(AwsConstants.IMAGE_ENDPOINT)
public class S3AwsController {

    private final S3ImageUploadService s3ImageUploadService;
    private final ImageMetadataService imageMetadataService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(@RequestParam("image") MultipartFile image) {
        s3ImageUploadService.uploadImage(image);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping(value = "/{filename}")
    public ResponseEntity<InputStreamResource> getImageByName(@PathVariable("filename") String fileName) {
        ContentFile imageContent = s3ImageUploadService.getImageByName(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + imageContent.getFilename());
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(imageContent.getContentLength())
                .body(new InputStreamResource(new ByteArrayInputStream(imageContent.getContent())));
    }

    @DeleteMapping(value = "/{filename}")
    public ResponseEntity<Void> deleteImageByName(@PathVariable("filename") String fileName) {
        s3ImageUploadService.deleteFileByName(fileName);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping(value = "/metadata")
    public ResponseEntity<ImageMetadata> getRandomImageMetadata() {
        return ResponseEntity
                .ok()
                .body(imageMetadataService.getRandomMetadata());
    }
}
