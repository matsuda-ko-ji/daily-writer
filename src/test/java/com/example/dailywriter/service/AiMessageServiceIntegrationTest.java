package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.client.AiResponseParser;
import com.example.dailywriter.client.MockAiTextGenerator;
import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

import tools.jackson.databind.ObjectMapper;

class AiMessageServiceIntegrationTest {

    /**
     * 実際のクラスを組み合わせて、
     * モックAIによる文章生成が成功すること
     */
    @Test
    void generateReturnsMockAiMessage() {

        // ① 実際のクラスを生成
        PromptBuilder promptBuilder =
                new PromptBuilder();

        MockAiTextGenerator aiTextGenerator =
                new MockAiTextGenerator();

        AiResponseParser responseParser =
                new AiResponseParser(new ObjectMapper());

        // ② テスト対象を生成
        AiMessageService service = new AiMessageService(
                promptBuilder,
                aiTextGenerator,
                responseParser
        );

        // ③ リクエストを作成
        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaの学習を行った",
                Tone.NORMAL
        );

        // ④ 文章生成を実行
        String result = service.generate(request);

        // ⑤ 生成結果を確認
        assertEquals(
                "【モックAI】文章生成の動作確認に成功しました。",
                result
        );
    }
}