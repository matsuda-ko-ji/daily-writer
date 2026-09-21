package com.example.dailywriter.form;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;

public class MessageForm {

    private MessageType type;

    private String workContent;

    private Tone tone;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public boolean isReportContentEmpty() {

        return type == MessageType.REPORT
                && (workContent == null || workContent.isBlank());
    }

    public Tone getTone() {
        return tone;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }
}