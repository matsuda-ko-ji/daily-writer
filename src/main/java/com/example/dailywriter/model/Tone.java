package com.example.dailywriter.model;

public enum Tone {

    NORMAL("標準"),
    POLITE("丁寧"),
    CONCISE("簡潔");

    private final String displayName;

    Tone(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}