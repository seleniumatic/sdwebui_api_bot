package com.seleniumatic.sd.common;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
    public static String getTestResourceFileContent(String filename) throws IOException {
            
       InputStream defaultInputStream = AppConfig.class.getClassLoader().getResourceAsStream(filename);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(defaultInputStream));
    }
}
