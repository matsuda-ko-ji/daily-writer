package com.example.dailywriter.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class RestClientConfigTest {

    @Test
    void restClientBuilderCanBuildRestClient() {

        RestClientConfig config =
                new RestClientConfig();

        RestClient.Builder builder =
                config.restClientBuilder();

        assertNotNull(builder);

        RestClient restClient = builder.build();

        assertNotNull(restClient);
    }
}