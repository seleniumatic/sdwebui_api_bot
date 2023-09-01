package com.seleniumatic.sd.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.seleniumatic.sd.App;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Util {
    public static String sendGET(URL url) throws IOException
    {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    public static String postJson(String apiUrl, String jsonContent) throws IOException 
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);

        // Set JSON content
        StringEntity stringEntity = new StringEntity(jsonContent, ContentType.APPLICATION_JSON);

        httpPost.setEntity(stringEntity);

        // Execute the request
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        // Convert the response entity to a string
        String responseBody = EntityUtils.toString(responseEntity);

        return responseBody;
    }

    public static String getApplicationPath() throws Exception
    {
        URL jarLocation = App.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = Paths.get(jarLocation.toURI()).getParent().toString();

        return jarPath;
    }

    public static String readFileFromJarLocation(String fileName) throws Exception
    {
        String inputFilePath = getApplicationPath() + File.separator + "json_input";
        
        String filePath = Paths.get(inputFilePath, fileName).toString();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    public static String readFileFromPath(String filePath) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    // public static String processFilesInFolders(String folderPath)
    // {
    //     File folder = new File(folderPath);
    //     File[] files = folder.listFiles();

    //     if (files != null) {
    //         if (files[0].isFile()) {
    //             return files[0].getName();
    //         }
    //     }


    // }

    public static JsonNode getJsonImageNode(String jsonRespose) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonRespose).get("images");
    }

    public static void decodeAndSaveImage(JsonNode jsonNode) throws Exception
    {
        byte[] decodedBytes;
        String filename;

        for (JsonNode image : jsonNode) {
            decodedBytes = Base64.getDecoder().decode(image.asText());
            filename = generateUniqueFileName("sdapi", "png");

            try (FileOutputStream fos = new FileOutputStream(getApplicationPath() + File.separator + "image_output" + File.separator + filename)) {
                fos.write(decodedBytes);
                fos.close();
                System.out.println(filename + " written successfully.");
            }
        }
    }

    public static void createApplicationFolder(String folderName) throws Exception 
    {
        String outputFolderPath = getApplicationPath() + File.separator + folderName;

        if (!folderExists(outputFolderPath)) {
            if (createFolder(outputFolderPath)) {
                System.out.println(folderName + " folder created successfully.");
            } else {
                System.out.println("Failed to create output folder.");
            }
        } else {
            System.out.println(folderName + " folder already exists.");
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

        String fileName = filenamePrefix + "_" + timestamp + "." + extension;

        return fileName;
    }
}
