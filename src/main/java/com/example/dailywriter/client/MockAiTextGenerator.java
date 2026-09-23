package com.example.dailywriter.client;

import org.springframework.stereotype.Component;

/**
 * 外部APIへ通信せず、固定のAIレスポンスを返すクラス
 */
@Component
public class MockAiTextGenerator implements AiTextGenerator {

    @Override
    public String generate(String prompt) {

        return """
                {
                  "status": "completed",
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "【モックAI】文章生成の動作確認に成功しました。"
                        }
                      ]
                    }
                  ]
                }
                """;
    }
}