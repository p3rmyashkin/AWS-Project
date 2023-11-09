package com.permiashkin.aws.application.providers;

import com.permiashkin.aws.application.data.ImageMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ImageDownloadUrlProviderTest {

    private EC2MetadataProvider ec2MetadataProvider;
    private ImageDownloadUrlProvider downloadUrlProvider;

    @BeforeEach
    void setup() {
        this.ec2MetadataProvider = mock(EC2MetadataProvider.class);
        this.downloadUrlProvider = new ImageDownloadUrlProvider("800", this.ec2MetadataProvider);
    }

    @Test
    void givenImageMetadata_whenProvideDownloadUrl_thenDownloadUrlIsProvided() {
        // GIVEN
        ImageMetadata imageMetadata = new ImageMetadata();
        imageMetadata.setName("name");
        imageMetadata.setFileExtension("png");

        when(ec2MetadataProvider.getPublicIpAddress())
                .thenReturn("62.4.36.126");

        // WHEN
        String downloadUrlForImage = downloadUrlProvider.getDownloadUrlForImage(imageMetadata);

        // THEN
        assertThat(downloadUrlForImage).isEqualTo("62.4.36.126:800/aws/api/v1/image/name.png");
    }


}
