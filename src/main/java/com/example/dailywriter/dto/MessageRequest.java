package com.example.dailywriter.dto;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

public record MessageRequest(
        MessageType type,
        String workContent,
        Tone tone
) {
}