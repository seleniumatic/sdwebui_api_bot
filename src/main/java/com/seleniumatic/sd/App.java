package com.seleniumatic.sd;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.seleniumatic.sd.common.Util;
import com.fasterxml.jackson.databind.JsonNode;

public class App 
{
    private static final String apiUrl = "http://127.0.0.1:7860/sdapi/v1/txt2img";

    public static void main( String[] args ) throws Exception
    {
        setUpFolders();

        System.out.print("Enter number of minutes to run: ");
        Scanner scanner = new Scanner(System.in);
        Integer numberOfMins = scanner.nextInt();
        scanner.close();

        doTheWork3();
    }

    private static void setUpFolders() throws Exception
    {
        Util.createApplicationFolder("json_input");
        Util.createApplicationFolder("image_output");
    }

    private static void doTheWork() throws Exception
    {
        System.out.print("Enter number of minutes to run: ");

        String inputFilePath = Util.getApplicationPath() + File.separator + "json_input" + File.separator + "txt2img.json";
        String jsonBody = Util.readFileFromPath(inputFilePath);
      
        try (Scanner scanner = new Scanner(System.in)) {
            final int runDurationMins = Integer.parseInt(scanner.nextLine());
        
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            boolean shouldStop[] = {false};

            executor.schedule(() -> {
                shouldStop[0] = true;
                System.out.println("Application is shutting down after " + runDurationMins + " minutes.");
                executor.shutdown();
            }, runDurationMins, TimeUnit.MINUTES);

            int count = 0;
            while (!shouldStop[0] && count <= runDurationMins * 60) {
                count++;
                System.out.println("Running application... request: " + count);
                    
                //logger.info("Running application... request: " + count);

                try {
                    String response = Util.postJson(apiUrl, jsonBody);

                    JsonNode imageNode = Util.getJsonImageNode(response);

                    Util.decodeAndSaveImage(imageNode);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Thread.sleep(10000); // delay to not overload endpoint
            }
        }
    }

    private static void doTheWork2(Integer runDurationMins) throws Exception
    {

        String inputFilePath = Util.getApplicationPath() + File.separator + "json_input" + File.separator + "txt2img.json";
        String jsonBody = Util.readFileFromPath(inputFilePath);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        boolean shouldStop[] = {false};

        executor.schedule(() -> {
            shouldStop[0] = true;
            System.out.println("Application is shutting down after " + runDurationMins + " minutes.");
            executor.shutdown();
        }, runDurationMins, TimeUnit.MINUTES);

        int count = 0;
        while (!shouldStop[0] && count <= runDurationMins * 60) {
            count++;
            System.out.println("Running application... request: " + count);

            try {
                String response = Util.postJson(apiUrl, jsonBody);

                JsonNode imageNode = Util.getJsonImageNode(response);

                Util.decodeAndSaveImage(imageNode);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.sleep(10000); // delay to not overload endpoint
        }
    }

    private static void doTheWork3() throws Exception
    {
        String inputFilePath = Util.getApplicationPath() + File.separator + "json_input";
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Checking for new files...");
            try {
                processFilesInFolder(inputFilePath);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
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

                    try {
                        String response = Util.postJson(apiUrl, jsonBody);

                        JsonNode imageNode = Util.getJsonImageNode(response);

                        Util.decodeAndSaveImage(imageNode);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (files.length > 1) {
                    System.out.println("Waiting 10 seconds before proecssing the next file...");
                    Thread.sleep(10000); // delay to not overload endpoint
                }
            }
        }
    }
}
