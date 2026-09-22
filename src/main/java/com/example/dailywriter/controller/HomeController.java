package com.example.dailywriter.controller;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.exception.InvalidMessageRequestException;
import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.form.MessageForm;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger =
        LoggerFactory.getLogger(HomeController.class);

    public HomeController(
            MessageService messageService,
            NewsService newsService
    ) {
        this.messageService = messageService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String index(Model model) {

        // 選択値が引き継がれていない場合のみ初期値を設定
        if (!model.containsAttribute("selectedType")) {
            model.addAttribute(
                    "selectedType",
                    MessageType.START
            );
        }

        if (!model.containsAttribute("selectedTone")) {
            model.addAttribute(
                    "selectedTone",
                    Tone.NORMAL
            );
        }

        try {

            List<News> newsList = newsService.getLatestNews();

            model.addAttribute("newsList", newsList);

        } catch (NewsFetchException e) {

            logger.warn("ニュースの取得に失敗しました。", e);

            model.addAttribute("newsList", List.of());

            model.addAttribute(
                    "newsError",
                    "ニュースを取得できませんでした。時間をおいて再度お試しください。"
            );
        }

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
                            ? Tone.NORMAL
                            : form.getTone()
            );
    }
}