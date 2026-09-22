package com.example.dailywriter.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {

        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        // サーバーとの接続確立を待つ時間
        requestFactory.setConnectTimeout(
                Duration.ofSeconds(3)
        );

        // レスポンスの読み取りを待つ時間
        requestFactory.setReadTimeout(
                Duration.ofSeconds(5)
        );

        return RestClient.builder()
                .requestFactory(requestFactory);
    }
}