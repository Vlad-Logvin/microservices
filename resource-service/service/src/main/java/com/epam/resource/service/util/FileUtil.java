package com.epam.resource.service.util;

import com.epam.resource.exception.impl.FileConverterException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Component
public class FileUtil {
    public File convertToFile(MultipartFile file) {
        File convertedFile = new File("TEMP.MP3");
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
            return convertedFile;
        } catch (Exception e) {
            throw new FileConverterException(e, "Exception was thrown during converting multipart file to file", 400);
        }
    }

    public void delete(File file) {
        try {
            file.delete();
        } catch (Exception ignored) {
        }
    }
}
