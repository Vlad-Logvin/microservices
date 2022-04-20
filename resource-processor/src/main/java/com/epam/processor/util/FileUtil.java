package com.epam.processor.util;

import com.epam.processor.exception.impl.FileWritingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileUtil {
    public File getFile(byte[] content) {
        File file = new File("temp.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
            return file;
        } catch (IOException e) {
            throw new FileWritingException(e, "Exception was thrown during writing content to file", 500);
        }
    }

    public void delete(File file) {
        try {
            file.delete();
        } catch (Exception ignored) {
        }
    }
}
