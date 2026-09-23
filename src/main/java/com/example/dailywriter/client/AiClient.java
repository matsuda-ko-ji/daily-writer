package com.example.dailywriter.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import com.example.dailywriter.config.AiConfig;
import com.example.dailywriter.dto.AiRequest;

public class AiClient implements AiTextGenerator {

    private final RestClient restClient;
    private final AiConfig aiConfig;
    private final String apiUrl;
    private final String model;

    public AiClient(
            RestClient.Builder restClientBuilder,
            AiConfig aiConfig,
            @Value("${daily-writer.ai.url}") String apiUrl,
            @Value("${daily-writer.ai.model:}") String model
    ) {
        this.restClient = restClientBuilder.build();
        this.aiConfig = aiConfig;
        this.apiUrl = apiUrl;
        this.model = model;
    }

    @Override
    public String generate(String prompt) {

        if (!aiConfig.isConfigured()) {
            throw new IllegalStateException(
                    "AI APIキーが設定されていません。"
            );
        }

        if (model == null || model.isBlank()) {
            throw new IllegalStateException(
                    "AIモデルが設定されていません。"
            );
        }

        AiRequest request = new AiRequest(
                model,
                prompt
        );

        return restClient.post()
                .uri(apiUrl)
                .header(
                        "Authorization",
                        "Bearer " + aiConfig.getApiKey()
                )
                .body(request)
                .retrieve()
                .body(String.class);
    }
}