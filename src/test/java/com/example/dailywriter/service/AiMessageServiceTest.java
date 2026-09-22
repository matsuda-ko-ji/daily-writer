package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.client.AiClient;
import com.example.dailywriter.client.AiResponseParser;
import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

class AiMessageServiceTest {

    @Test
    void generateConnectsAllComponents() {

        PromptBuilder promptBuilder =
                mock(PromptBuilder.class);

        AiClient aiClient =
                mock(AiClient.class);

        AiResponseParser responseParser =
                mock(AiResponseParser.class);

        AiMessageService service = new AiMessageService(
                promptBuilder,
                aiClient,
                responseParser
        );

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "不具合を修正した。",
                Tone.POLITE
        );

        when(promptBuilder.build(request))
                .thenReturn("生成用プロンプト");

        when(aiClient.generate("生成用プロンプト"))
                .thenReturn("JSONレスポンス");

        when(responseParser.parse("JSONレスポンス"))
                .thenReturn("生成された文章");

        String result = service.generate(request);

        assertEquals("生成された文章", result);

        verify(promptBuilder).build(request);

        verify(aiClient).generate("生成用プロンプト");

        verify(responseParser).parse("JSONレスポンス");
    }
}