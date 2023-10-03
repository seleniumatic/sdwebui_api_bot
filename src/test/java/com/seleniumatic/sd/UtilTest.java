package com.seleniumatic.sd;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.seleniumatic.sd.common.TestUtil;
import com.seleniumatic.sd.common.Util;

public class UtilTest {
    @Test
    public void testImageFileCreation() throws URISyntaxException, IOException{

        String testFilePath = TestUtil.getTestResourceFilePath() + "sample_txt2img_response.json";

        String response = Util.readJsonFileFromPath(testFilePath);

        JsonNode imageNode = Util.getJsonImageNode(response);

        String expectedFilePath = Util.decodeAndSaveImage(imageNode);

        System.out.println(imageNode);

        File expectedImageFile = new File(expectedFilePath);

        assertTrue(expectedImageFile.exists());
    }
}
