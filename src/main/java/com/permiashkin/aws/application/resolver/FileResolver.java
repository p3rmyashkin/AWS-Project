package com.permiashkin.aws.application.resolver;

import com.permiashkin.aws.application.model.File;
import org.springframework.stereotype.Service;

@Service
public class FileResolver {

    public File getFileByOriginalFileName(String originalFilename) {
        int lastIndex = originalFilename.lastIndexOf(".");
        String name = originalFilename.substring(0, lastIndex);
        String extension = originalFilename.substring(lastIndex + 1);

        return File.builder()
                .name(name)
                .extension(extension)
                .build();
    }
}
