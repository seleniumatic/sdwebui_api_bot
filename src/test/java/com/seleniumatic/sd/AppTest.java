package com.seleniumatic.sd;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.seleniumatic.sd.common.AppConfig;
import com.seleniumatic.sd.common.FileProcessor;
import com.seleniumatic.sd.common.SdService;
import com.seleniumatic.sd.common.TestUtil;
import com.seleniumatic.sd.common.Util;

public class AppTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setup() throws URISyntaxException, IOException {
        Util.createApplicationFolder("image_output");
        Util.createApplicationFolder("json_input");
        Util.createSampleTxt2ImageFile();

        String responseBody = TestUtil.getTestResourceFileContent("sample_txt2img_response.json");

        stubFor(post(urlEqualTo("/post"))
         .willReturn(aResponse()
         .withStatus(200)
         .withHeader("Content-Type", "application/json")
         .withBody(responseBody)));
    }

    // @Test
    // public void testDoTheWork() throws URISyntaxException {

    //     Integer INTERVAL_API_REQUEST = 4;
    //     String API_URL = "http://localhost:8089/post";
    //     Integer INTERVAL_FILE_PROCESSING = 4;

    //     App.doTheWork(API_URL, INTERVAL_API_REQUEST, INTERVAL_FILE_PROCESSING);
    // }

    // @Test
    // public void testFileProcess() throws IOException, InterruptedException, URISyntaxException {

    //     Integer INTERVAL_EXECUTOR_PERIOD = AppConfig.getApiRequestIntervalSeconds();
    //     Integer INTERVAL_FILE_PROCESSING = AppConfig.getFileProcessingIntervalSeconds();
    //     String API_URL = "http://localhost:8089/post";
    //     String inputFilePath = Util.getAppExecutionPath() + File.separator + "json_input";

    //     HttpClient httpClient = HttpClient.newHttpClient();
    //     SdService sdService = new SdService(httpClient, API_URL, "");
    //     FileProcessor fileProcessor = new FileProcessor(sdService, inputFilePath, INTERVAL_FILE_PROCESSING);

    //     String expectedFilePath = App.inputFileProcessor(sdService, testFilePath);
    //     File expectedImageFile = new File(expectedFilePath);

    //     assertTrue(expectedImageFile.exists());
    // }    
}
