package com.example.dailywriter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.example.dailywriter.config.AiConfig;

class AiClientTest {

    private static final String API_URL =
            "https://api.openai.com/v1/responses";

    /**
     * POSTでAI APIへリクエストを送信できること
     */
    @Test
    void generateSendsPostRequest() {

        RestClient.Builder builder = RestClient.builder();

        MockRestServiceServer server =
                MockRestServiceServer
                        .bindTo(builder)
                        .build();

        AiClient client = new AiClient(
                builder,
                new AiConfig("dummy-api-key"),
                API_URL,
                "test-model"
        );

        server.expect(requestTo(API_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(
                        "Authorization",
                        "Bearer dummy-api-key"
                ))
                .andExpect(jsonPath(
                        "$.model"
                ).value("test-model"))
                .andExpect(jsonPath(
                        "$.input"
                ).value("日報を作成してください。"))
                .andRespond(
                        withSuccess(
                                "{\"id\":\"response-123\"}",
                                MediaType.APPLICATION_JSON
                        )
                );

        String response = client.generate(
                "日報を作成してください。"
        );

        assertEquals(
                "{\"id\":\"response-123\"}",
                response
        );

        server.verify();
    }

    /**
     * APIキーが未設定の場合、通信前に例外が発生すること
     */
    @Test
    void generateRejectsMissingApiKey() {

        AiClient client = new AiClient(
                RestClient.builder(),
                new AiConfig(""),
                API_URL,
                "test-model"
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> client.generate("テスト")
        );

        assertEquals(
                "AI APIキーが設定されていません。",
                exception.getMessage()
        );
    }

    /**
     * モデルが未設定の場合、通信前に例外が発生すること
     */
    @Test
    void generateRejectsMissingModel() {

        AiClient client = new AiClient(
                RestClient.builder(),
                new AiConfig("dummy-api-key"),
                API_URL,
                ""
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> client.generate("テスト")
        );

        assertEquals(
                "AIモデルが設定されていません。",
                exception.getMessage()
        );
    }
}