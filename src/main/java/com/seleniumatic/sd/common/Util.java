package com.seleniumatic.sd.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
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

    public static String httpGetRequest(String url) throws URISyntaxException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        String responseBody = null;

        try {
            // Send the GET request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            responseBody = response.body();
        } catch (InterruptedException|IOException e) {
            logger.error("An error occured while making Http GET request: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

        return responseBody;
    }

    public static String httpPostRequest(String url, String jsonBody) throws URISyntaxException
    {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        String responseBody = null;

        try {
            // Send the POST request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            responseBody = response.body();
        } catch (InterruptedException|IOException e) {
            logger.error("An error occured while making Http POST request: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

        return responseBody;
    }

    public static String getApplicationPath() throws URISyntaxException
    {
        URL jarLocation = App.class.getProtectionDomain().getCodeSource().getLocation();
        return Paths.get(jarLocation.toURI()).getParent().toString();

    }

    public static String readFileFromJarLocation(String fileName) throws URISyntaxException, IOException
    {
        String inputFilePath = getApplicationPath() + File.separator + "json_input";
        
        String filePath = Paths.get(inputFilePath, fileName).toString();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    public static String readFileFromPath(String filePath) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    public static JsonNode getJsonImageNode(String jsonRespose) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonRespose).get("images");
    }

    public static void decodeAndSaveImage(JsonNode jsonNode) throws URISyntaxException, IOException
    {
        byte[] decodedBytes;
        String filename;

        for (JsonNode image : jsonNode) {
            decodedBytes = Base64.getDecoder().decode(image.asText());
            filename = generateUniqueFileName("sdapi", "png");

            try (FileOutputStream fos = new FileOutputStream(getApplicationPath() + File.separator + "image_output" + File.separator + filename)) {
                fos.write(decodedBytes);
                logger.info("{} written successfully.", filename);
            }
        }
    }

    public static void createApplicationFolder(String folderName) throws URISyntaxException  
    {
        String outputFolderPath = getApplicationPath() + File.separator + folderName;

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

    public static void createSampleTxt2ImageFile() throws URISyntaxException 
    {
        String fileName = "txt2img.json";
        String sourceFilePath = "sample_txt2img.json";
        String destinationFilePath = getApplicationPath() + File.separator + "json_input" + File.separator + fileName;

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
    }
}
