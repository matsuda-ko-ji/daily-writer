package com.example.dailywriter.service;

import com.example.dailywriter.model.MessageType;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String generate(
            MessageType type,
            String workContent) {

        return switch (type) {
            case START -> "おはようございます。本日もよろしくお願いします。";
            case END -> "本日の業務を終了します。お疲れさまでした。";
            case REPORT -> generateReport(workContent);
        };
    }

    private String generateReport(String workContent) {

        if (workContent == null || workContent.isBlank()) {
            return "今日やったことを入力してください。";
        }

        return "本日は" + workContent
                + "。今回の経験を今後の業務に活かしていきます。";
    }
}