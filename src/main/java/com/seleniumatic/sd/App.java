package com.seleniumatic.sd;

import java.io.File;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.seleniumatic.sd.common.AppConfig;
import com.seleniumatic.sd.common.FileProcessor;
import com.seleniumatic.sd.common.SdService;
import com.seleniumatic.sd.common.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    static final Logger logger = LogManager.getLogger(App.class);
    public static void main( String[] args ) throws Exception
    {

        initialSetup();
        
        Integer INTERVAL_EXECUTOR_PERIOD = AppConfig.getApiRequestIntervalSeconds();
        String API_URL = AppConfig.getTxt2ImgEndpoint();
        Integer INTERVAL_FILE_PROCESSING = AppConfig.getFileProcessingIntervalSeconds();
        String inputFolderPath = Util.getAppExecutionPath() + File.separator + "json_input";
        String outputFolderPath = Util.getAppExecutionPath() + File.separator + "image_output";


        HttpClient httpClient = HttpClient.newHttpClient();
        SdService sdService = new SdService(httpClient, API_URL, "");
        FileProcessor fileProcessor = new FileProcessor(sdService, inputFolderPath, outputFolderPath, INTERVAL_FILE_PROCESSING);

        System.out.println("Bot running... (Ctrl+C to exit)");

        doTheWork(fileProcessor, INTERVAL_EXECUTOR_PERIOD);
    }

    private static void initialSetup() throws URISyntaxException {
        Util.createApplicationFolder("json_input");
        Util.createApplicationFolder("image_output");
        Util.createApplicationFolder("config");
        Util.createSampleTxt2ImageFile();
    }

    public static void doTheWork(FileProcessor fileProcessor, Integer executorPeriod) {
        logger.info("Starting file processing job...");
        Runnable task = () -> {
            try {
                fileProcessor.process();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("An error occurred while during processing: {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(task, 0, executorPeriod, TimeUnit.SECONDS);
    }
}
