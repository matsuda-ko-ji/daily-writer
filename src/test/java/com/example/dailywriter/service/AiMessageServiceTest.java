package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.client.AiResponseParser;
import com.example.dailywriter.client.AiTextGenerator;
import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

class AiMessageServiceTest {

    @Test
    void generateUsesAiTextGeneratorAndParsesResponse() {

        // ① 依存するクラスをモック化
        PromptBuilder promptBuilder =
                mock(PromptBuilder.class);

        AiTextGenerator aiTextGenerator =
                mock(AiTextGenerator.class);

        AiResponseParser responseParser =
                mock(AiResponseParser.class);

        // ② テスト対象を生成
        AiMessageService service = new AiMessageService(
                promptBuilder,
                aiTextGenerator,
                responseParser
        );

        // ③ リクエストを作成
        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaを学習した",
                Tone.NORMAL
        );

        // ④ モックの戻り値を設定
        when(promptBuilder.build(request))
                .thenReturn("生成用プロンプト");

        when(aiTextGenerator.generate("生成用プロンプト"))
                .thenReturn("JSONレスポンス");

        when(responseParser.parse("JSONレスポンス"))
                .thenReturn("生成された文章");

        // ⑤ テスト対象を実行
        String result = service.generate(request);

        // ⑥ 結果を検証
        assertEquals("生成された文章", result);

        // ⑦ 処理の呼び出しを検証
        verify(promptBuilder).build(request);

        verify(aiTextGenerator)
                .generate("生成用プロンプト");

        verify(responseParser)
                .parse("JSONレスポンス");
    }
}