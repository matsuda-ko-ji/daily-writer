package com.example.dailywriter.dto;

public record AiRequest(
        String model,
        String input
) {
}