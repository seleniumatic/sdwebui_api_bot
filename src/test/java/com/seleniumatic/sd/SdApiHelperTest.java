package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.seleniumatic.sd.common.SdApiClient;

public class SdApiHelperTest {

    private WireMockServer wireMockServer;
    private SdApiClient SdApiHelper;

    @Before
    public void setup() {
        WireMockConfiguration wireMockConfig = WireMockConfiguration.options().port(8080);
        wireMockServer = new WireMockServer(wireMockConfig);
        wireMockServer.start();

       SdApiHelper = new SdApiClient("http://localhost:8080/data", "{\"prompt\": \"cat\"}");
    }

    @After
    public void tearDown() {
        wireMockServer.shutdown();;
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

        stubFor(get(urlEqualTo("/data"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResult)));

        String actualResult = SdApiHelper.httpGetRequest();
       
        assertEquals(expectedResult, actualResult);
    }



    
}
