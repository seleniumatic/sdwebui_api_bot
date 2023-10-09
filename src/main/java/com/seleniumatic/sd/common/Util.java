package com.seleniumatic.sd.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.seleniumatic.sd.App;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Util {

    static final Logger logger = LogManager.getLogger(Util.class);

    private Util() {}

    public static String getAppExecutionPath() throws URISyntaxException {
        URL jarLocation = App.class.getProtectionDomain().getCodeSource().getLocation();
        return Paths.get(jarLocation.toURI()).getParent().toString();

    }

    public static String readJsonFileFromPath(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    public static JsonNode getJsonImageNode(String jsonContent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonContent).get("images");
    }
    
    public static String decodeAndSaveImage(JsonNode jsonNode, String targetFolderPath) throws IOException {
        byte[] decodedBytes;
        String filename = "";
        String filePath = "";

        for (JsonNode image : jsonNode) {
            decodedBytes = Base64.getDecoder().decode(image.asText());
            filename = generateUniqueFileName("sdapi", "png");
            filePath = targetFolderPath + File.separator + filename;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(decodedBytes);
                logger.info("{} written successfully.", filename);
            }
        }
        return filePath;
    }

    public static void createApplicationFolder(String folderName) throws URISyntaxException {
        String outputFolderPath = getAppExecutionPath() + File.separator + folderName;

        if (!folderExists(outputFolderPath)) {
            if (createFolder(outputFolderPath)) {
                logger.info("{} folder created successfully.", folderName);
            } else {
                logger.info("Failed to create folder: {}", folderName);
            }
        } else {
            logger.info("{} folder already exists.", folderName);
        }
    }

    public static boolean folderExists(String path) {
        File folder = new File(path);
        return folder.exists() && folder.isDirectory();
    }

    public static boolean createFolder(String path) {
        File folder = new File(path);
        return folder.mkdir();
    }

    public static String generateUniqueFileName(String filenamePrefix, String extension)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        return filenamePrefix + "_" + timestamp + "." + extension;
    }

    public static File createSampleTxt2ImageFile() throws URISyntaxException {
        String fileName = "txt2img.json";
        String sourceFilePath = "sample_txt2img.json";
        String destinationFilePath = getAppExecutionPath() + File.separator + "json_input" + File.separator + fileName;

        File file = new File(destinationFilePath);

        // Check if the file already exists
        if (file.exists()) {
            logger.info("Input file already exist.  Will not overwrite.");
        } else {
            try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(sourceFilePath);
                    OutputStream outputStream = new FileOutputStream(destinationFilePath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                logger.info("Sample txt2img.json input file created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("An error occurred while attemppting to create sample input file: {}", e.getMessage(), e);
            }
        }

        return file;
    }
}
