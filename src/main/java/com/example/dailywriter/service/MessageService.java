package com.example.dailywriter.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

  public String generateStartMessage() {
    String message = "おはようございます。本日もよろしくお願いいたします。";
    return message;
  }
  
}
