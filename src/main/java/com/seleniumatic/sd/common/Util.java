package com.seleniumatic.sd.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
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
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static String getApplicationPath() throws URISyntaxException
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

    public static String readFileFromPath(String filePath) throws JsonProcessingException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(objectMapper.readTree(new File(filePath)));
    }

    public static JsonNode getJsonImageNode(String jsonRespose) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonRespose).get("images");
    }

    public static void decodeAndSaveImage(JsonNode jsonNode) throws FileNotFoundException, URISyntaxException, IOException
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

    public static void createSampleTxt2ImageFile() throws Exception 
    {
        String fileName = "txt2img.json";
        String sourceFilePath = "sample_txt2img.json";
        String destinationFilePath = getApplicationPath() + File.separator + "json_input" + File.separator + fileName;

        File file = new File(destinationFilePath);

        // Check if the file already exists
        if (file.exists()) {
            System.out.println("Input file already exist.  Will not overwrite.");

        } else {
            try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(sourceFilePath);
                    OutputStream outputStream = new FileOutputStream(destinationFilePath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Sample txt2img.json input file created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
