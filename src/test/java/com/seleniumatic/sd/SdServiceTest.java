package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.seleniumatic.sd.common.SdService;

public class SdServiceTest {

    private String fakeEndpoint = "http://endpoint.com";
    private String testRequestBody = "{\"prompt\": \"cat\"}";
    private SdService sdService;
    private HttpClient mockHttpClient;
    private HttpResponse<String> mockHttpResponse;

    @BeforeClass
    @SuppressWarnings("unchecked")
    public void setup() {
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockHttpResponse = Mockito.mock(HttpResponse.class);
        sdService = new SdService(mockHttpClient, fakeEndpoint, testRequestBody);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGet() throws InterruptedException, IOException, URISyntaxException {
        Mockito.when(mockHttpResponse.body()).thenReturn("GET Response Body"); 
        Mockito.when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);

        String actualReponseBody = sdService.get();
        assertEquals("GET Response Body", actualReponseBody);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPost() throws InterruptedException, IOException, URISyntaxException {
        Mockito.when(mockHttpResponse.body()).thenReturn("POST Response Body"); 
        Mockito.when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);

        String actualReponseBody = sdService.post();
        assertEquals("POST Response Body", actualReponseBody);
    }

    @Test
    public void testGetter() {
        assertEquals("http://endpoint.com", fakeEndpoint);
        assertEquals("{\"prompt\": \"cat\"}", testRequestBody);
    }
}
