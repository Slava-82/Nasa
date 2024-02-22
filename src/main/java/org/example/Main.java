package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        String url = "https://api.nasa.gov/planetary/apod" +
                "?api_key=KyUAMZVA7GRdQGkpjXfjr6BqYoA9n4pCxFbp3Hd4" + "&date=2024-02-22";
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofDays(5000))    // максимальное время ожидание подключения к серверу
                        .setConnectTimeout(Timeout.ofDays(30000))    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        HttpGet request = new HttpGet(url);
            CloseableHttpResponse response =httpClient.execute(request);

        Nasa answer = mapper.readValue(response.getEntity().getContent(), Nasa.class);

        HttpGet imageGet = new HttpGet(answer.url);
//        String filename = "Image-" + LocalDate.now() + ".jpg";
        String[] urlSplited = answer.url.split("/");
        String filename = urlSplited[urlSplited.length - 1];

        CloseableHttpResponse image = httpClient.execute(imageGet);
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        image.getEntity().writeTo(fileOutputStream);





    }
}