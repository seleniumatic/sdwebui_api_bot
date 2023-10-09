package com.seleniumatic.sd.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SdService {
    static final Logger logger = LogManager.getLogger(SdService.class);

    
    private final HttpClient httpClient;
    private String url;
    private String body;


    public SdService(HttpClient httpClient, String url, String body) {
        this.httpClient = httpClient;
        this.url = url;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String get() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(this.url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        String responseBody = null;

        logger.info("Calling URL: {} ...", this.url);
        try {
            // Send the GET request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            responseBody = response.body();
        } catch (InterruptedException|IOException e) {
            logger.error("An error occured while making Http GET request: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return responseBody;
    }

    public String post() throws URISyntaxException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(this.url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(this.body))
            .build();

        String responseBody = null;

        logger.info("Calling URL: {} ...", this.url);
        try {
            // Send the POST request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            responseBody = response.body();
        } catch (InterruptedException|IOException e) {
            logger.error("An error occured while making Http POST request: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return responseBody;
    }
}
