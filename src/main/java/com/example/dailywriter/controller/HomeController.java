package com.example.dailywriter.controller;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MessageService messageService;

    public HomeController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(Model model) {
        String message = messageService.generate(MessageType.START);

        model.addAttribute("message", message);

        return "index";
    }
}