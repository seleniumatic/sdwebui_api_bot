package com.seleniumatic.sd;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.seleniumatic.sd.common.AppConfig;
import com.seleniumatic.sd.common.Util;
import com.fasterxml.jackson.databind.JsonNode;

public class App {

    static Integer pollingInterval = AppConfig.getPollingIntervalSec();
    static String apiUrl = AppConfig.getTxt2ImgEndpoint();

    public static void main( String[] args ) throws Exception
    {
        setUpFolders();

        System.out.println("Bot running...");

        doTheWork();
    }

    private static void setUpFolders() throws Exception
    {
        Util.createApplicationFolder("json_input");
        Util.createApplicationFolder("image_output");
        Util.createApplicationFolder("config");
        Util.createSampleTxt2ImageFile();
    }

    private static void doTheWork() throws Exception
    {
        String inputFilePath = Util.getApplicationPath() + File.separator + "json_input";
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Checking for new files...");
            try {
                processFilesInFolder(inputFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executor.scheduleAtFixedRate(task, 0, pollingInterval, TimeUnit.SECONDS);
    }

    public static void processFilesInFolder(String folderPath) throws Exception
    {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Processing file: " + file.getName());
                    
                    String jsonBody = Util.readFileFromPath(file.getPath());

                    System.out.println("Calling api...");

                    String response = Util.postJson(apiUrl, jsonBody);

                    JsonNode imageNode = Util.getJsonImageNode(response);

                    Util.decodeAndSaveImage(imageNode);
                }

                if (files.length > 1) {
                    System.out.println("Waiting 10 seconds before proecssing the next file...");
                    Thread.sleep(10000); // delay to not overload endpoint
                }
            }
        }
    }
}
