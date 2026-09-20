package com.example.dailywriter.controller;

import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final MessageService messageService;

    public HomeController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(Model model) {

        if (!model.containsAttribute("selectedType")) {
            model.addAttribute("selectedType", MessageType.START);
        }

        return "index";
    }

    @PostMapping("/generate")
    public String generate(
            @RequestParam MessageType type,
            RedirectAttributes redirectAttributes) {

        String message = messageService.generate(type);

        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("selectedType", type);

        return "redirect:/";
    }
}