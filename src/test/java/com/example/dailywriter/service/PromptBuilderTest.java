package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

class PromptBuilderTest {

    private final PromptBuilder promptBuilder =
            new PromptBuilder();

    /**
     * 開始挨拶の指示が含まれること
     */
    @Test
    void buildIncludesStartInstruction() {

        MessageRequest request = new MessageRequest(
                MessageType.START,
                "",
                Tone.NORMAL
        );

        String prompt = promptBuilder.build(request);

        assertTrue(
                prompt.contains("業務開始時の挨拶")
        );

        assertTrue(
                prompt.contains("自然で標準的な文体")
        );
    }

    /**
     * 終了挨拶の指示が含まれること
     */
    @Test
    void buildIncludesEndInstruction() {

        MessageRequest request = new MessageRequest(
                MessageType.END,
                "",
                Tone.CONCISE
        );

        String prompt = promptBuilder.build(request);

        assertTrue(
                prompt.contains("業務終了時の挨拶")
        );

        assertTrue(
                prompt.contains("簡潔な文体")
        );
    }

    /**
     * 日報の所感と作業内容が含まれること
     */
    @Test
    void buildIncludesReportAndWorkContent() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "ゲスト購入画面の不具合を修正した。",
                Tone.POLITE
        );

        String prompt = promptBuilder.build(request);

        assertTrue(
                prompt.contains("日報の所感")
        );

        assertTrue(
                prompt.contains("丁寧な文体")
        );

        assertTrue(
                prompt.contains(
                        "ゲスト購入画面の不具合を修正した。"
                )
        );
    }

    /**
     * 入力されていない事実を創作しない指示が含まれること
     */
    @Test
    void buildIncludesSafetyInstruction() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "テストを実施した。",
                Tone.NORMAL
        );

        String prompt = promptBuilder.build(request);

        assertTrue(
                prompt.contains(
                        "入力されていない事実や成果を創作しないでください。"
                )
        );
    }

    /**
     * リクエストがnullの場合は例外が発生すること
     */
    @Test
    void buildRejectsNullRequest() {

        assertThrows(
                IllegalArgumentException.class,
                () -> promptBuilder.build(null)
        );
    }
}