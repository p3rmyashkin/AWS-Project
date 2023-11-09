package com.permiashkin.aws.application.providers;

import com.permiashkin.aws.application.consts.AwsConstants;
import com.permiashkin.aws.application.data.ImageMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ImageDownloadUrlProvider {

    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String SEMICOLON = ":";

    private final String serverPort;
    private final EC2MetadataProvider ec2MetadataProvider;

    public ImageDownloadUrlProvider(@Value("${server.port}") String serverPort,
                                    EC2MetadataProvider ec2MetadataProvider) {
        this.serverPort = serverPort;
        this.ec2MetadataProvider = ec2MetadataProvider;
    }

    public String getDownloadUrlForImage(String filename) {
        String ipAddress = ec2MetadataProvider.getPublicIpAddress();
        if (StringUtils.hasText(ipAddress)) {
            return new StringBuilder(ipAddress)
                    .append(SEMICOLON)
                    .append(serverPort)
                    .append(SLASH)
                    .append(AwsConstants.IMAGE_ENDPOINT)
                    .append(SLASH)
                    .append(filename)
                    .toString();
        }
        return null;
    }

    public String getDownloadUrlForImage(ImageMetadata imageMetadata) {
        return getDownloadUrlForImage(imageMetadata.getName() + DOT + imageMetadata.getFileExtension());
    }
}
