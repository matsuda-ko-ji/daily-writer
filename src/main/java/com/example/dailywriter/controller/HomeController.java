package com.example.dailywriter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.form.MessageForm;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.AiMessageService;
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;

@Controller
public class HomeController {

    private static final Logger logger =
            LoggerFactory.getLogger(HomeController.class);

    private final MessageService messageService;
    private final NewsService newsService;
    private final AiMessageService aiMessageService;

    public HomeController(
            MessageService messageService,
            NewsService newsService,
            AiMessageService aiMessageService
    ) {
        this.messageService = messageService;
        this.newsService = newsService;
        this.aiMessageService = aiMessageService;
    }

    /**
     * トップ画面表示
     */
    @GetMapping("/")
    public String index(Model model) {

        // 文章の種類の初期値
        if (!model.containsAttribute("selectedType")) {

            model.addAttribute(
                    "selectedType",
                    MessageType.START
            );
        }

        // 文体の初期値
        if (!model.containsAttribute("selectedTone")) {

            model.addAttribute(
                    "selectedTone",
                    Tone.NORMAL
            );
        }

        // 生成方法の初期値
        if (!model.containsAttribute("selectedGenerationMethod")) {

            model.addAttribute(
                    "selectedGenerationMethod",
                    "template"
            );
        }

        // ニュース取得
        try {

            List<News> newsList =
                    newsService.getLatestNews();

            model.addAttribute(
                    "newsList",
                    newsList
            );

        } catch (NewsFetchException e) {

            logger.warn(
                    "ニュースの取得に失敗しました。",
                    e
            );

            model.addAttribute(
                    "newsList",
                    List.of()
            );

            model.addAttribute(
                    "newsError",
                    "ニュースを取得できませんでした。時間をおいて再度お試しください。"
            );
        }

        return "index";
    }

    /**
     * 文章生成
     */
    @PostMapping("/generate")
    public String generate(
            @ModelAttribute MessageForm form,
            RedirectAttributes redirectAttributes
    ) {

        try {

            // フォームの入力値からリクエストを作成
            MessageRequest request = new MessageRequest(
                    form.getType(),
                    form.getWorkContent(),
                    form.getTone()
            );

            // 生成方法を取得
            String generationMethod =
                    form.getGenerationMethod();

            String message;

            // 生成方法によって呼び出すサービスを切り替える
            if ("ai".equals(generationMethod)) {

                message = aiMessageService.generate(request);

            } else if ("template".equals(generationMethod)) {

                message = messageService.generate(request);

            } else {

                throw new IllegalArgumentException(
                        "生成方法が不正です。"
                );
            }

            // 生成結果を画面へ引き継ぐ
            redirectAttributes.addFlashAttribute(
                    "message",
                    message
            );

        } catch (IllegalArgumentException e) {

            // 入力値・生成方法が不正な場合
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        // 入力値を保持
        addFormAttributes(
                redirectAttributes,
                form
        );

        return "redirect:/";
    }

    /**
     * フォームの入力値をリダイレクト先へ引き継ぐ
     */
    private void addFormAttributes(
            RedirectAttributes redirectAttributes,
            MessageForm form
    ) {

        // 文章の種類
        redirectAttributes.addFlashAttribute(
                "selectedType",
                form.getType()
        );

        // 作業内容
        redirectAttributes.addFlashAttribute(
                "workContent",
                form.getWorkContent()
        );

        // 文体
        redirectAttributes.addFlashAttribute(
                "selectedTone",
                form.getTone() == null
                        ? Tone.NORMAL
                        : form.getTone()
        );

        // 生成方法
        redirectAttributes.addFlashAttribute(
                "selectedGenerationMethod",
                form.getGenerationMethod()
        );
    }
}
