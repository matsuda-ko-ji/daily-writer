package com.example.dailywriter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;

class MockAiTextGeneratorTest {

    /**
     * モックAIが返すJSONから生成文章を取得できること
     */
    @Test
    void generateReturnsValidResponse() {

        // ① モックAIを生成
        MockAiTextGenerator generator =
                new MockAiTextGenerator();

        // ② JSON解析クラスを生成
        AiResponseParser parser =
                new AiResponseParser(new ObjectMapper());

        // ③ モックAIからJSONを取得
        String json = generator.generate(
                "日報を作成してください。"
        );

        // ④ JSONを解析
        String result = parser.parse(json);

        // ⑤ 期待する文章と一致するか確認
        assertEquals(
                "【モックAI】文章生成の動作確認に成功しました。",
                result
        );
    }
}