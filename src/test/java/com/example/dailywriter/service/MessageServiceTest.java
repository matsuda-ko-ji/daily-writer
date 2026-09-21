package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

class MessageServiceTest {

    @Test
    void generateReturnsStartMessage() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.START,
                "",
                Tone.NORMAL
        );

        String actual = messageService.generate(request);

        assertEquals(
                "おはようございます。本日もよろしくお願いします。",
                actual
        );
    }

    @Test
    void generateReturnsEndMessage() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.END,
                "",
                Tone.NORMAL
        );

        String actual = messageService.generate(request);

        assertEquals(
                "本日の業務を終了します。お疲れさまでした。",
                actual
        );
    }

    @Test
    void generateReturnsReportMessage() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaを学習した",
                Tone.NORMAL
        );

        String actual = messageService.generate(request);

        assertEquals(
                "本日はJavaを学習した。今回の経験を今後の業務に活かしていきます。",
                actual
        );
    }

    @Test
    void generateReturnsPoliteReportMessage() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaを学習した",
                Tone.POLITE
        );

        String actual = messageService.generate(request);

        assertEquals(
                "本日はJavaを学習した。今回学んだ内容を今後の業務に活かしてまいります。",
                actual
        );
    }

    @Test
    void generateReturnsConciseReportMessage() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaを学習した",
                Tone.CONCISE
        );

        String actual = messageService.generate(request);

        assertEquals(
                "Javaを学習した。",
                actual
        );
    }

    @Test
    void generateUsesNormalToneWhenToneIsNull() {

        MessageService messageService = new MessageService();

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaを学習した",
                null
        );

        String actual = messageService.generate(request);

        assertEquals(
                "本日はJavaを学習した。今回の経験を今後の業務に活かしていきます。",
                actual
        );
    }
}