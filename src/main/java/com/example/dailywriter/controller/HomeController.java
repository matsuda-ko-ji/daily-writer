package com.example.dailywriter.controller;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.exception.InvalidMessageRequestException;
import com.example.dailywriter.form.MessageForm;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final MessageService messageService;
    private final NewsService newsService;

    public HomeController(
            MessageService messageService,
            NewsService newsService
    ) {
        this.messageService = messageService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute(
                "selectedType",
                MessageType.START
        );

        model.addAttribute(
                "selectedTone",
                Tone.NORMAL
        );

        List<News> newsList = newsService.getLatestNews();

        model.addAttribute("newsList", newsList);

        return "index";
    }

    @PostMapping("/generate")
    public String generate(
            @ModelAttribute MessageForm form,
            RedirectAttributes redirectAttributes) {

        try {

            MessageRequest request = new MessageRequest(
                    form.getType(),
                    form.getWorkContent(),
                    form.getTone()
            );

            String message = messageService.generate(request);

            redirectAttributes.addFlashAttribute(
                    "message",
                    message
            );

        } catch (InvalidMessageRequestException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        addFormAttributes(redirectAttributes, form);

        return "redirect:/";
    }

    private void addFormAttributes(
        RedirectAttributes redirectAttributes,
        MessageForm form) {

        redirectAttributes.addFlashAttribute(
                "selectedType",
                form.getType()
        );

        redirectAttributes.addFlashAttribute(
                "workContent",
                form.getWorkContent()
        );

        redirectAttributes.addFlashAttribute(
                "selectedTone",
                form.getTone() == null
                        ? Tone.NORMAL.name()
                        : form.getTone().name()
        );
    }
}