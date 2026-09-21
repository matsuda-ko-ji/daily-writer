package com.example.dailywriter.service;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String generate(MessageRequest request) {

        MessageType type = request.type();
        String workContent = request.workContent();
        Tone tone = request.tone();

        if (tone == null) {
            tone = Tone.NORMAL;
        }

        return switch (type) {

            case START ->
                    "おはようございます。本日もよろしくお願いします。";

            case END ->
                    "本日の業務を終了します。お疲れさまでした。";

            case REPORT ->
                    generateReport(workContent, tone);
        };
    }

    private String generateReport(
            String workContent,
            Tone tone) {

        if (workContent == null || workContent.isBlank()) {
            return "今日やったことを入力してください。";
        }

        return switch (tone) {

            case NORMAL ->
                    "本日は" + workContent
                            + "。今回の経験を今後の業務に活かしていきます。";

            case POLITE ->
                    "本日は" + workContent
                            + "。今回学んだ内容を今後の業務に活かしてまいります。";

            case CONCISE ->
                    workContent + "。";
        };
    }
}