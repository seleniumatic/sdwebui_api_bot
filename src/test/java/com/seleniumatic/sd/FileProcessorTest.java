package com.seleniumatic.sd;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.seleniumatic.sd.common.FileProcessor;
import com.seleniumatic.sd.common.SdService;
import com.seleniumatic.sd.common.TestUtil;
import com.seleniumatic.sd.common.Util;

import org.mockito.Mockito;

public class FileProcessorTest {

    SdService mockSdService;
    String testInputFilePath;
    String testOutputFilePath;
    Integer processIntervalSec = 1;

    @Before
    public void setup() throws URISyntaxException, IOException {
        Util.createApplicationFolder("image_output");
        Util.createApplicationFolder("json_input");
        Util.createSampleTxt2ImageFile();

        String responseBody = TestUtil.getTestResourceFileContent("sample_txt2img_response.json");

        mockSdService = Mockito.mock(SdService.class);
        Mockito.when(mockSdService.post()).thenReturn(responseBody);

        testInputFilePath = Util.getAppExecutionPath() + File.separator + "json_input";
        testOutputFilePath = Util.getAppExecutionPath() + File.separator + "image_output";
    }

    @Test
    public void testFileProcessing() throws InterruptedException, IOException, URISyntaxException {
        FileProcessor fileProcessor = new FileProcessor(mockSdService, testInputFilePath, testOutputFilePath, processIntervalSec);

        String saveImagePath = fileProcessor.process();

        File expectedImageFile = new File(saveImagePath);

        assertTrue(expectedImageFile.exists());
    }
}
