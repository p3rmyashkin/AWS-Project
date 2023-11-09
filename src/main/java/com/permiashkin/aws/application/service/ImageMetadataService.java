package com.permiashkin.aws.application.service;

import com.permiashkin.aws.application.model.File;
import com.permiashkin.aws.application.data.ImageMetadata;
import com.permiashkin.aws.application.repository.ImageMetadataRepository;
import com.permiashkin.aws.application.resolver.FileResolver;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@AllArgsConstructor
public class ImageMetadataService {

    private final ImageMetadataRepository imageMetadataRepository;
    private final FileResolver fileResolver;

    public ImageMetadata saveMetadata(String originalFilename, long size) {
        File file = fileResolver.getFileByOriginalFileName(originalFilename);

        ImageMetadata metadata = imageMetadataRepository.findByName(file.getName())
                .map(imageMetadata -> updateMetadata(file, size, imageMetadata))
                .orElseGet(() -> createImageMetadata(file, size));

        imageMetadataRepository.save(metadata);
        return metadata;
    }

    public void deleteMetadata(String fileName) {
        File file = fileResolver.getFileByOriginalFileName(fileName);
        imageMetadataRepository.deleteByName(file.getName());
    }


    public ImageMetadata getRandomMetadata() {
        return imageMetadataRepository.findRandomMetadata()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private ImageMetadata createImageMetadata(File file, long size) {
        ImageMetadata imageMetadata = new ImageMetadata();
        return updateMetadata(file, size, imageMetadata);
    }

    private ImageMetadata updateMetadata(File file, long size, ImageMetadata imageMetadata) {
        imageMetadata.setLastUpdated(new Date());
        imageMetadata.setName(file.getName());
        imageMetadata.setFileExtension(file.getExtension());
        imageMetadata.setSize(size);
        return imageMetadata;
    }
}
