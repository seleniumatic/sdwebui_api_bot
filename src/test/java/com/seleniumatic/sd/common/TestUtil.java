package com.seleniumatic.sd.common;

import java.net.URISyntaxException;

public class TestUtil {

    static String testResourcesRelativePath = "/test-classes/com/seleniumatic/sd/resources/";

    public static String getTestResourceFilePath() throws URISyntaxException {
            
        String resourcesPath = Util.getAppExecutionPath() + testResourcesRelativePath;
        
        return resourcesPath;
    }
}
