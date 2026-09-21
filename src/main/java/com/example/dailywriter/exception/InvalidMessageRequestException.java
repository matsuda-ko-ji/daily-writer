package com.example.dailywriter.exception;

public class InvalidMessageRequestException
        extends IllegalArgumentException {

    public InvalidMessageRequestException(String message) {
        super(message);
    }
}