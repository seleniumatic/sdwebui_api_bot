package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumatic.sd.common.JsonToImageWriter;
import com.seleniumatic.sd.common.TestUtil;
import com.seleniumatic.sd.common.Util;

public class JsonToImageWriterTest {
    String testOutputFilePath;
    String responseBody;
    
    @Before
    public void setup() throws URISyntaxException, IOException {
        Util.createApplicationFolder("image_output");
        testOutputFilePath = Util.getAppExecutionPath() + File.separator + "image_output";
        responseBody = TestUtil.getTestResourceFileContent("sample_txt2img_response.json");
    }

    @Test
    public void testImageFileCreation() throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode imageNode = objectMapper.readTree(responseBody).get("images");

        JsonToImageWriter jsonToImageWriter = new JsonToImageWriter(imageNode, testOutputFilePath);

        String saveImagePath = jsonToImageWriter.write();

        File expectedImageFile = new File(saveImagePath);

        assertTrue(expectedImageFile.exists());
    }

    @Test
    public void getterSetter() throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode imageNode = objectMapper.readTree(responseBody).get("images");

        JsonToImageWriter jsonToImageWriter = new JsonToImageWriter(imageNode, testOutputFilePath);

        assertEquals(jsonToImageWriter.getJsonNode(), imageNode);
        assertEquals(jsonToImageWriter.getTargetFolder(), testOutputFilePath);
    }
}
