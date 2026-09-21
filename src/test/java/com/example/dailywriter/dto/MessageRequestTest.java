package com.example.dailywriter.dto;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    // 前後の空白が除去される
    @Test
    void constructorRemovesLeadingAndTrailingSpaces() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "  Javaを学習した  ",
                Tone.NORMAL
        );

        assertEquals(
                "Javaを学習した",
                request.workContent()
        );
    }

    // 文章の途中の空白は保持される
    @Test
    void constructorKeepsSpacesInsideText() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                "  Java と Spring を学習した  ",
                Tone.NORMAL
        );

        assertEquals(
                "Java と Spring を学習した",
                request.workContent()
        );
    }

    // nullでも例外が発生しない
    @Test
    void constructorAcceptsNullWorkContent() {

        MessageRequest request = new MessageRequest(
                MessageType.REPORT,
                null,
                Tone.NORMAL
        );

        assertNull(request.workContent());
    }
}