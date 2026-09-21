package com.example.dailywriter.dto;

import com.example.dailywriter.exception.InvalidMessageRequestException;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void constructorAllowsNullEndContent() {

        MessageRequest request = new MessageRequest(
                MessageType.END,
                null,
                Tone.NORMAL
        );

        assertNull(request.workContent());
    }

    @Test
    void constructorRejectsEmptyReportContent() {

        InvalidMessageRequestException exception = assertThrows(
                InvalidMessageRequestException.class,
                () -> new MessageRequest(
                        MessageType.REPORT,
                        "",
                        Tone.NORMAL
                )
        );

        assertEquals(
                "今日やったことを入力してください。",
                exception.getMessage()
        );
    }

    @Test
    void constructorRejectsBlankReportContent() {

        assertThrows(
                InvalidMessageRequestException.class,
                () -> new MessageRequest(
                        MessageType.REPORT,
                        "   ",
                        Tone.NORMAL
                )
        );
    }

    @Test
    void constructorRejectsNullReportContent() {

        assertThrows(
                InvalidMessageRequestException.class,
                () -> new MessageRequest(
                        MessageType.REPORT,
                        null,
                        Tone.NORMAL
                )
        );
    }

    @Test
    void constructorAllowsEmptyStartContent() {

        MessageRequest request = new MessageRequest(
                MessageType.START,
                "",
                Tone.NORMAL
        );

        assertEquals("", request.workContent());
    }
}