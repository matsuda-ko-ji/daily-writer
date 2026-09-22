package com.example.dailywriter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;

class AiResponseParserTest {

    private final AiResponseParser parser =
            new AiResponseParser(new ObjectMapper());

    @Test
    void parseReturnsGeneratedText() {

        String json = """
                {
                  "status": "completed",
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "output_text",
                          "text": "本日は不具合を修正しました。"
                        }
                      ]
                    }
                  ]
                }
                """;

        String result = parser.parse(json);

        assertEquals(
                "本日は不具合を修正しました。",
                result
        );
    }

    @Test
    void parseRejectsEmptyResponse() {

        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("")
        );
    }

    @Test
    void parseRejectsIncompleteResponse() {

        String json = """
                {
                  "status": "incomplete",
                  "output": []
                }
                """;

        assertThrows(
                IllegalStateException.class,
                () -> parser.parse(json)
        );
    }

    @Test
    void parseRejectsMissingOutputText() {

        String json = """
                {
                  "status": "completed",
                  "output": []
                }
                """;

        assertThrows(
                IllegalStateException.class,
                () -> parser.parse(json)
        );
    }

    @Test
    void parseRejectsInvalidJson() {

        assertThrows(
                IllegalStateException.class,
                () -> parser.parse("{invalid")
        );
    }
}