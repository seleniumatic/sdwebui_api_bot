package com.seleniumatic.sd.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonToImageWriter {
    static final Logger logger = LogManager.getLogger(JsonToImageWriter.class);

    private String targetFolder;
    private JsonNode jsonNode;

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public void setJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public JsonToImageWriter(JsonNode jsonNode, String targetFolder) {
        this.jsonNode = jsonNode;
        this.targetFolder = targetFolder;
    }

    public String write() throws IOException {
        byte[] decodedBytes;
        String filePath = "";

        for (JsonNode image : jsonNode) {
            decodedBytes = Base64.getDecoder().decode(image.asText());
            filePath = this.targetFolder + File.separator + Util.generateUniqueFileName("sdapi", "png");
        
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(decodedBytes);
                logger.info("File written successfully to: {}", filePath);
            }
        }
        return filePath;
    }
}
