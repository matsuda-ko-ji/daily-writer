package com.example.dailywriter.service;

import com.example.dailywriter.model.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceTest {

    @Test
    void generateReturnsStartMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.START);

        assertEquals(
                "おはようございます。本日もよろしくお願いします。",
                actual
        );
    }

    @Test
    void generateReturnsEndMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.END);

        assertEquals(
                "本日の業務を終了します。お疲れさまでした。",
                actual
        );
    }

    @Test
    void generateReturnsReportMessage() {

        MessageService messageService = new MessageService();

        String actual = messageService.generate(MessageType.REPORT);

        assertEquals(
                "本日の業務を振り返り、学んだことを次回に活かします。",
                actual
        );
    }
}