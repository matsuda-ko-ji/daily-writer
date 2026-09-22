package com.example.dailywriter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiConfig {

    private final String apiKey;

    public AiConfig(
            @Value("${daily-writer.ai.api-key:}") String apiKey
    ) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}