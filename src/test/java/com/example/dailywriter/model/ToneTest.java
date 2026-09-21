package com.example.dailywriter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToneTest {

    @Test
    void normalHasDisplayName() {

        assertEquals(
                "標準",
                Tone.NORMAL.getDisplayName()
        );
    }

    @Test
    void politeHasDisplayName() {

        assertEquals(
                "丁寧",
                Tone.POLITE.getDisplayName()
        );
    }

    @Test
    void conciseHasDisplayName() {

        assertEquals(
                "簡潔",
                Tone.CONCISE.getDisplayName()
        );
    }
}