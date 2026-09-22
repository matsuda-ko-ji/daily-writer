package com.example.dailywriter.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class AiResponseParser {

    private final ObjectMapper objectMapper;

    public AiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String parse(String json) {

        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException(
                    "AI APIのレスポンスが空です。"
            );
        }

        try {

            JsonNode root = objectMapper.readTree(json);

            if (!"completed".equals(
                    root.path("status").stringValue()
            )) {
                throw new IllegalStateException(
                        "AIの文章生成が完了していません。"
                );
            }

            JsonNode output = root.path("output");

            if (!output.isArray()) {
                throw new IllegalStateException(
                        "AI APIの出力形式が不正です。"
                );
            }

            List<String> texts = new ArrayList<>();

            for (JsonNode item : output) {

                if (!"message".equals(
                        item.path("type").stringValue()
                )) {
                    continue;
                }

                JsonNode content = item.path("content");

                if (!content.isArray()) {
                    continue;
                }

                for (JsonNode part : content) {

                    if ("output_text".equals(
                            part.path("type").stringValue()
                    )) {

                        String text = part.path("text").stringValue("");

                        if (!text.isBlank()) {
                            texts.add(text);
                        }
                    }
                }
            }

            if (texts.isEmpty()) {
                throw new IllegalStateException(
                        "AIの生成文章が取得できませんでした。"
                );
            }

            return String.join("\n", texts);

        } catch (JacksonException e) {

            throw new IllegalStateException(
                    "AI APIのJSON解析に失敗しました。",
                    e
            );
        }
    }
}