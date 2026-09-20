package com.example.dailywriter.service;

import com.example.dailywriter.model.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceTest {

    @Test
    void generateReturnsStartMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.START, "");

        assertEquals(
                "おはようございます。本日もよろしくお願いします。",
                actual
        );
    }

    @Test
    void generateReturnsEndMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.END, "");

        assertEquals(
                "本日の業務を終了します。お疲れさまでした。",
                actual
        );
    }

    @Test
    void generateReturnsReportMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.REPORT, "本番リリース手順書を修正した");

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
                ""
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
                "   "
        );

        assertEquals(
                "今日やったことを入力してください。",
                actual
        );
    }

    @Test
    void generateReturnsMessageWhenReportContentIsNull() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(
                MessageType.REPORT,
                null
        );

        assertEquals(
                "今日やったことを入力してください。",
                actual
        );
    }
}