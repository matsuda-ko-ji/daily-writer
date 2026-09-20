package com.example.dailywriter.service;

import com.example.dailywriter.model.MessageType;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String generate(MessageType type) {
        return switch (type) {
            case START -> "おはようございます。本日もよろしくお願いします。";
            case END -> "本日の業務を終了します。お疲れさまでした。";
            case REPORT -> "本日の業務を振り返り、学んだことを次回に活かします。";
        };
    }
}