package com.example.dailywriter.dto;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageRequestTest {

    @Test
    void recordReturnsConstructorValues() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "Javaのrecordを学習した",
                Tone.POLITE
        );

        assertEquals(
                MessageType.REPORT,
                request.type()
        );

        assertEquals(
                "Javaのrecordを学習した",
                request.workContent()
        );

        assertEquals(
                Tone.POLITE,
                request.tone()
        );
    }
}