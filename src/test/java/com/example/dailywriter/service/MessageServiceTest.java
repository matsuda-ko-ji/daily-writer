package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

class MessageServiceTest {

    @Test
    void generateReturnsStartMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.START,
                "",
                Tone.NORMAL
        );

        assertEquals(
                "おはようございます。本日もよろしくお願いします。",
                actual
        );
    }

    @Test
    void generateReturnsEndMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.END,
                "",
                Tone.NORMAL
        );

        assertEquals(
                "本日の業務を終了します。お疲れさまでした。",
                actual
        );
    }

    @Test
    void generateReturnsReportMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                "本番リリース手順書を修正した",
                Tone.NORMAL
        );

        assertEquals(
                "本日は本番リリース手順書を修正した。今回の経験を今後の業務に活かしていきます。",
                actual
        );
    }

    @Test
    void generateReturnsMessageWhenReportContentIsEmpty() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                "",
                Tone.NORMAL
        );

        assertEquals(
                "今日やったことを入力してください。",
                actual
        );
    }

    @Test
    void generateReturnsMessageWhenReportContentIsBlank() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                "   ",
                Tone.NORMAL
        );

        assertEquals(
                "今日やったことを入力してください。",
                actual
        );
    }

    @Test
    void generateReturnsPoliteReportMessage() {

    MessageService messageService = new MessageService();

    String actual = messageService.generate(
            MessageType.REPORT,
            "Javaを学習した",
            Tone.POLITE
    );

    assertEquals(
            "本日はJavaを学習した。今回学んだ内容を今後の業務に活かしてまいります。",
            actual
    );
    }

    @Test
    void generateReturnsConciseReportMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                "Javaを学習した",
                Tone.CONCISE
        );

        assertEquals(
                "Javaを学習した。",
                actual
        );
    }

    @Test
    void generateUsesNormalToneWhenToneIsNull() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                "Javaを学習した",
                null
        );

        assertEquals(
                "本日はJavaを学習した。今回の経験を今後の業務に活かしていきます。",
                actual
        );
    }
}