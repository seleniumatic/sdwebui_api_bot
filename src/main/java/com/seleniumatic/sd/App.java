package com.seleniumatic.sd;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.seleniumatic.sd.common.AppConfig;
import com.seleniumatic.sd.common.SdApiClient;
import com.seleniumatic.sd.common.Util;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    static final Logger logger = LogManager.getLogger(App.class);
    static Integer INTERVAL_API_REQUEST = AppConfig.getApiRequestIntervalSeconds();
    static String API_URL = AppConfig.getTxt2ImgEndpoint();
    static Integer INTERVAL_FILE_PROCESSING = AppConfig.getFileProcessingIntervalSeconds();

    public static void main( String[] args ) throws Exception
    {
        setUp();

        System.out.println("Bot running... (Ctrl+C to exit)");

        doTheWork();
    }

    private static void setUp() throws URISyntaxException
    {
        Util.createApplicationFolder("json_input");
        Util.createApplicationFolder("image_output");
        Util.createApplicationFolder("config");
        Util.createSampleTxt2ImageFile();
    }

    private static void doTheWork() throws URISyntaxException
    {
        String inputFilePath = Util.getAppExecutionPath() + File.separator + "json_input";
        
        Runnable task = () -> {

            logger.info("Checking for input files...");
            try {
                processFilesInFolder(inputFilePath);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("An error occurred while processing input file: {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(task, 0, INTERVAL_API_REQUEST, TimeUnit.SECONDS);
    }

    public static void processFilesInFolder(String folderPath) throws IOException, InterruptedException, URISyntaxException
    {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        SdApiClient sdApiClient;
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    logger.info("Processing file: {}",file.getName());
                    
                    String jsonBody = Util.readJsonFileFromPath(file.getPath());
                    sdApiClient = new SdApiClient(API_URL, jsonBody);

                    logger.info("Calling URL: {} ...", API_URL);
                    String response = sdApiClient.httpPostRequest();

                    JsonNode imageNode = Util.getJsonImageNode(response);

                    Util.decodeAndSaveImage(imageNode);
                }

                if (files.length > 1) {
                    logger.info("Waiting {} seconds before proecssing the next file...", INTERVAL_API_REQUEST);

                    Thread.sleep(INTERVAL_FILE_PROCESSING); // delay to not overload endpoint
                }
            }
        }
    }
}
