package com.example.dailywriter.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AiConfigTest {

    /**
     * APIキーが設定されている場合
     */
    @Test
    void isConfiguredReturnsTrueWhenApiKeyExists() {

        AiConfig config = new AiConfig("dummy-api-key");

        assertEquals(
                "dummy-api-key",
                config.getApiKey()
        );

        assertTrue(config.isConfigured());
    }

    /**
     * APIキーが空文字の場合
     */
    @Test
    void isConfiguredReturnsFalseWhenApiKeyIsEmpty() {

        AiConfig config = new AiConfig("");

        assertFalse(config.isConfigured());
    }

    /**
     * APIキーが空白のみの場合
     */
    @Test
    void isConfiguredReturnsFalseWhenApiKeyIsBlank() {

        AiConfig config = new AiConfig("   ");

        assertFalse(config.isConfigured());
    }

    /**
     * APIキーがnullの場合
     */
    @Test
    void isConfiguredReturnsFalseWhenApiKeyIsNull() {

        AiConfig config = new AiConfig(null);

        assertFalse(config.isConfigured());
    }
}