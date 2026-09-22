package com.example.dailywriter.service;

import org.springframework.stereotype.Service;

import com.example.dailywriter.client.AiClient;
import com.example.dailywriter.client.AiResponseParser;
import com.example.dailywriter.dto.MessageRequest;

@Service
public class AiMessageService {

    private final PromptBuilder promptBuilder;
    private final AiClient aiClient;
    private final AiResponseParser responseParser;

    public AiMessageService(
            PromptBuilder promptBuilder,
            AiClient aiClient,
            AiResponseParser responseParser
    ) {
        this.promptBuilder = promptBuilder;
        this.aiClient = aiClient;
        this.responseParser = responseParser;
    }

    public String generate(MessageRequest request) {

        String prompt = promptBuilder.build(request);

        String json = aiClient.generate(prompt);

        return responseParser.parse(json);
    }
}