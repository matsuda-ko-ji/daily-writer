package com.example.dailywriter.service;

import org.springframework.stereotype.Service;

import com.example.dailywriter.client.AiResponseParser;
import com.example.dailywriter.client.AiTextGenerator;
import com.example.dailywriter.dto.MessageRequest;

/**
 * AI文章生成処理を管理するサービス
 */
@Service
public class AiMessageService {

    private final PromptBuilder promptBuilder;
    private final AiTextGenerator aiTextGenerator;
    private final AiResponseParser responseParser;

    public AiMessageService(
            PromptBuilder promptBuilder,
            AiTextGenerator aiTextGenerator,
            AiResponseParser responseParser
    ) {
        this.promptBuilder = promptBuilder;
        this.aiTextGenerator = aiTextGenerator;
        this.responseParser = responseParser;
    }

    public String generate(MessageRequest request) {

        // プロンプトを作成
        String prompt = promptBuilder.build(request);

        // モックAIからJSONを取得
        String json = aiTextGenerator.generate(prompt);

        // JSONを解析して生成文章を取得
        return responseParser.parse(json);
    }
}