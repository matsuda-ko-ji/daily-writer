package com.example.dailywriter.form;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
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

    @Test
    void eachFormHasItsOwnWorkContent() {

        MessageForm form1 = new MessageForm();
        MessageForm form2 = new MessageForm();

        form1.setWorkContent("Javaを勉強した");
        form2.setWorkContent("Springを勉強した");

        assertEquals(
                "Javaを勉強した",
                form1.getWorkContent()
        );

        assertEquals(
                "Springを勉強した",
                form2.getWorkContent()
        );
    }

    @Test
    void twoReferencesCanPointToSameForm() {

        MessageForm form1 = new MessageForm();

        MessageForm form2 = form1;

        form1.setWorkContent("Javaを勉強した");

        assertEquals(
                "Javaを勉強した",
                form2.getWorkContent()
        );
    }

    @Test
    void getterReturnsToneSetBySetter() {

        MessageForm form = new MessageForm();

        form.setTone(Tone.POLITE);

        assertEquals(
                Tone.POLITE,
                form.getTone()
        );
    }
}