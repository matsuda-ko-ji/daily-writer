package com.example.dailywriter.dto;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

public record MessageRequest(
        MessageType type,
        String workContent,
        Tone tone
) {

    public MessageRequest {

        if (workContent != null) {
            workContent = workContent.strip();
        }

        if (type == MessageType.REPORT
                && (workContent == null || workContent.isBlank())) {

            throw new IllegalArgumentException(
                    "今日やったことを入力してください。"
            );
        }
    }
}