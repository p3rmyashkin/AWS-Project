package com.permiashkin.aws.application.repository;

import com.permiashkin.aws.application.data.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

    Optional<ImageMetadata> findByName(String imageName);

    @Query(nativeQuery = true, value = "SELECT * FROM image ORDER BY RANDOM() LIMIT 1;")
    Optional<ImageMetadata> findRandomMetadata();

    void deleteByName(String imageName);
}
