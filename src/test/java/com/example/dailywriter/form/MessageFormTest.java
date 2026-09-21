package com.example.dailywriter.form;

import com.example.dailywriter.model.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageFormTest {

    @Test
    void getterReturnsValuesSetBySetter() {

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("JUnitのテストを追加した");

        assertEquals(
                MessageType.REPORT,
                form.getType()
        );

        assertEquals(
                "JUnitのテストを追加した",
                form.getWorkContent()
        );
    }

    @Test
    void reportContentIsEmptyWhenContentIsEmpty() {

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("");

        assertTrue(form.isReportContentEmpty());
    }

    @Test
    void reportContentIsNotEmptyWhenContentExists() {

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("Bean Validationについて学習した");

        assertFalse(form.isReportContentEmpty());
    }

    @Test
    void reportContentIsNotEmptyForStartMessage() {

        MessageForm form = new MessageForm();

        form.setType(MessageType.START);
        form.setWorkContent("");

        assertFalse(form.isReportContentEmpty());
    }

    @Test
    void reportContentIsEmptyWhenContentIsNull() {

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent(null);

        assertTrue(form.isReportContentEmpty());
    }
}