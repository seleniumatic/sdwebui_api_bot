package com.seleniumatic.sd.common;

public class TestUtil {
    public static String GetFilePath(String relativeFilePath) {
    
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        
        return rootPath + relativeFilePath;
    }
}
