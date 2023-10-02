package com.seleniumatic.sd.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfig {

    static final Logger logger = LogManager.getLogger(AppConfig.class);

    private static Properties properties = new Properties();

    static String customPropertiesFolderName = "config";
    static String customPropertiesFileName = "application.properties";
    static String defaultPropertiesFileName = "default.properties";

    static String customPropertiesFilePath = "";
    static String defaultPropertiesFilePath = defaultPropertiesFileName;

    static 
        {try 
            {
                // Get the location of the currently executing JAR file
                String jarDirectory = Util.getApplicationPath();
                String configFolderPath = jarDirectory  + File.separator + customPropertiesFolderName;
                customPropertiesFilePath = configFolderPath + File.separator + customPropertiesFileName;

                // Check if the custom properties file exists
                File customPropertiesFile = new File(customPropertiesFilePath);
                
                if (customPropertiesFile.exists()) {
                    logger.info("Loading custom properties: {}", customPropertiesFileName);
                    loadPropertiesFromFile(customPropertiesFileName);
                } else {
                    logger.info("Loading default properties: {}", defaultPropertiesFileName);
                    loadPropertiesFromFile(defaultPropertiesFilePath);
                }
            // Access and use the properties
            logger.info("Property Values: {}", properties);
        } catch (Exception e) {
            logger.error("An error occurred while attempting to load app properties: {}", e.getMessage(), e);
        }   
    }

    private AppConfig() {}

    private static void loadPropertiesFromFile(String propertiesFilePath) throws IOException {
        try (InputStream defaultInputStream = AppConfig.class.getClassLoader().getResourceAsStream(propertiesFilePath)) {
            properties.load(defaultInputStream);
        }
    }

    public static String getTxt2ImgEndpoint() {
        return properties.getProperty("txt2img.endpoint");
    }

    public static Integer getApiRequestIntervalSeconds() {
        return Integer.parseInt(properties.getProperty("api.request.interval.seconds"));
    }

     public static Integer getFileProcessingIntervalSeconds() {
        return Integer.parseInt(properties.getProperty("file.process.interval.seconds"));
    }
}
