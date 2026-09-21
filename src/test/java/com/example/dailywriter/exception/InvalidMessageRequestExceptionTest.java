package com.example.dailywriter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class InvalidMessageRequestExceptionTest {

    @Test
    void constructorSetsMessage() {

        InvalidMessageRequestException exception =
                new InvalidMessageRequestException("入力が不正です。");

        assertEquals(
                "入力が不正です。",
                exception.getMessage()
        );
    }

    @Test
    void exceptionIsIllegalArgumentException() {

        InvalidMessageRequestException exception =
                new InvalidMessageRequestException("入力が不正です。");

        assertInstanceOf(
                IllegalArgumentException.class,
                exception
        );
    }
}