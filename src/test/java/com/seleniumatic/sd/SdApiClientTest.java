package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.seleniumatic.sd.common.SdApiClient;

public class SdApiClientTest {

    private SdApiClient sdApiHelper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @BeforeClass
    public void setup() {
        sdApiHelper = new SdApiClient("http://localhost:8089/data", "{\"prompt\": \"cat\"}");
    }

    @Test
    public void testHttpGetRequest() throws URISyntaxException {
        String expectedResult = "{\"message\":\"Get request content\"}";

        stubFor(get(urlEqualTo("/data"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResult)));

        String actualResult = sdApiHelper.httpGetRequest();
       
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHttpPostRequest() throws URISyntaxException {
        String expectedResult = "{\"message\":\"Post request content\"}";

        stubFor(post(urlEqualTo("/data"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResult)));

        String actualResult = sdApiHelper.httpPostRequest();
       
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testSdApiClientProperty() {
        String expectedUrl = "http://localhost:8089/data";
        String expectedBody = "{\"prompt\": \"cat\"}";

        assertEquals(expectedUrl, sdApiHelper.getUrl());
        assertEquals(expectedBody, sdApiHelper.getBody());
    }
}
