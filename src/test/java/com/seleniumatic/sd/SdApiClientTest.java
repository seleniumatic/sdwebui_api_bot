package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.seleniumatic.sd.common.SdApiClient;

public class SdApiClientTest {

    private SdApiClient SdApiHelper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setup() {
        SdApiHelper = new SdApiClient("http://localhost:8089/data", "{\"prompt\": \"cat\"}");
    }

    @Test
    public void testHttpGetRequest() throws URISyntaxException {
        String expectedResult = "{\"message\":\"Get request content\"}";

        stubFor(get(urlEqualTo("/data"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResult)));

        String actualResult = SdApiHelper.httpGetRequest();
       
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

        String actualResult = SdApiHelper.httpPostRequest();
       
        assertEquals(expectedResult, actualResult);
    }   
}
