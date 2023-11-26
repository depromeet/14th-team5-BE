package com.oing.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public class FileMockingUtils {
    private static final String FILE_PATH = "src/test/resources/images/";
    private static final String SINGLE_FILE_META_NAME = "file";

    public static MultipartFile createSingleMockMultipartFile(String fileName, String contentType) throws IOException {
        try (FileInputStream stream = new FileInputStream(FILE_PATH + fileName)) {
            return new MockMultipartFile(SINGLE_FILE_META_NAME, fileName, contentType, stream);
        }
    }
}
