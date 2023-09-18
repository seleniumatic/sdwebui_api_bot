package com.seleniumatic.sd.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.seleniumatic.sd.App;

public class AppConfig {

    static final Logger logger = LogManager.getLogger(App.class);

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
                    // Load custom properties from the custom file
                    try (InputStream customInputStream = new FileInputStream(customPropertiesFilePath)) {
                        properties.load(customInputStream);
                        // System.out.println("Custom properties loaded from: " + customPropertiesFilePath);
                        logger.info("Custom properties loaded from: " + customPropertiesFilePath);
                    }
                } else {
                    // Load default properties from the default file
                    try (InputStream defaultInputStream = AppConfig.class.getClassLoader().getResourceAsStream(defaultPropertiesFilePath)) {
                        properties.load(defaultInputStream);
                        // System.out.println("Default properties loaded from resource: " + defaultPropertiesFilePath);
                        logger.info("Default properties loaded from resource: " + defaultPropertiesFilePath);
                    }
            }
            // Access and use the properties
            String propertyValue = properties.toString();
            // System.out.println("Property Value: " + propertyValue);
            logger.info("Property Value: " + propertyValue);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred: {}", e.getMessage(), e);
        }   
    }

    public static String getTxt2ImgEndpoint() {
        return properties.getProperty("txt2img.endpoint");
    }

    public static Integer getPollingIntervalSec() {
        return Integer.parseInt(properties.getProperty("polling.interval.sec"));
    }
}
