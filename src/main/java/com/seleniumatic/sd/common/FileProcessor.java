package com.seleniumatic.sd.common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

public class FileProcessor {
    static final Logger logger = LogManager.getLogger(FileProcessor.class);

    private final SdService sdService;
    private final String inputFolder;
    private final String outputFolder;
    private final Integer processIntervalSec;

    public SdService getSdService() {
        return sdService;
    }

      public String getInputFolder() {
        return inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }
        
    public Integer getProcessIntervalSec() {
        return processIntervalSec;
    }

    public FileProcessor(SdService sdService, String inputFolder, String outputFolder, Integer processIntervalSec) {
        this.sdService = sdService;
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
        this.processIntervalSec = processIntervalSec;
    }

    public String process() throws IOException, InterruptedException, URISyntaxException{
        File folder = new File(this.inputFolder);
        File[] files = folder.listFiles();
        String saveImagePath = "";
        JsonToImageWriter jsonToImageWriter;
       
        logger.info("Checking for input files...");
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    logger.info("Processing file: {}", file.getName());
                    
                    String jsonBody = Util.readJsonFileFromPath(file.getPath());
                    sdService.setBody(jsonBody);

                    String response = sdService.post();

                    JsonNode imageNode = Util.getJsonImageNode(response);

                    jsonToImageWriter = new JsonToImageWriter(imageNode, this.outputFolder);
                    saveImagePath = jsonToImageWriter.write();
                }

                if (files.length > 1) {
                    logger.info("Waiting {} seconds before proecssing the next file...", this.processIntervalSec);

                    Thread.sleep(this.processIntervalSec); // delay to not overload endpoint
                }
            }
        }
       return saveImagePath;
    }
}
