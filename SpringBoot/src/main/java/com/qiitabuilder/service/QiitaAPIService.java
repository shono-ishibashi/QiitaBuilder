package com.qiitabuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * QiitaAPIと接続し、処理を行うクラス
 */

@Service
public class QiitaAPIService {

    @Autowired
    private RestTemplate restTemplate;

    public static final String URL = "https://qiita.com/api/v2/items";

    public void restTemplateTest() {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("body","# テスト");
        requestBody.put("title","テスト");

        RequestEntity<Map<String, String>> requestEntity = RequestEntity
                .post(URI.create(URL))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer 21c96b7e4607cf12e6bc9841815a4dd06e8b7ad8")
                .body(requestBody);
    }
}
