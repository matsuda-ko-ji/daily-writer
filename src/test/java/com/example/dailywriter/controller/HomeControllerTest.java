package com.example.dailywriter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.form.MessageForm;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.AiMessageService;
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;

class HomeControllerTest {

    /**
     * 定型文生成を選択した場合、
     * MessageServiceが呼び出されること
     */
    @Test
    void generateUsesTemplateService() {

        MessageService messageService =
                mock(MessageService.class);

        NewsService newsService =
                mock(NewsService.class);

        AiMessageService aiMessageService =
                mock(AiMessageService.class);

        HomeController controller = new HomeController(
                messageService,
                newsService,
                aiMessageService
        );

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("Javaを学習した");
        form.setTone(Tone.NORMAL);
        form.setGenerationMethod("template");

        when(messageService.generate(any(MessageRequest.class)))
                .thenReturn("テスト用の文章");

        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        String viewName = controller.generate(
                form,
                redirectAttributes
        );

        assertEquals(
                "redirect:/",
                viewName
        );

        assertEquals(
                "テスト用の文章",
                redirectAttributes.getFlashAttributes().get("message")
        );

        verify(messageService)
                .generate(any(MessageRequest.class));

        verify(aiMessageService, never())
                .generate(any(MessageRequest.class));

        assertEquals(
                "template",
                redirectAttributes.getFlashAttributes()
                        .get("selectedGenerationMethod")
        );
    }

    /**
     * AI生成を選択した場合、
     * AiMessageServiceが呼び出されること
     */
    @Test
    void generateUsesAiService() {

        MessageService messageService =
                mock(MessageService.class);

        NewsService newsService =
                mock(NewsService.class);

        AiMessageService aiMessageService =
                mock(AiMessageService.class);

        HomeController controller = new HomeController(
                messageService,
                newsService,
                aiMessageService
        );

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("不具合を修正した");
        form.setTone(Tone.POLITE);
        form.setGenerationMethod("ai");

        when(aiMessageService.generate(any(MessageRequest.class)))
                .thenReturn("AIが生成した文章");

        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        String viewName = controller.generate(
                form,
                redirectAttributes
        );

        assertEquals(
                "redirect:/",
                viewName
        );

        assertEquals(
                "AIが生成した文章",
                redirectAttributes.getFlashAttributes().get("message")
        );

        verify(aiMessageService)
                .generate(any(MessageRequest.class));

        verify(messageService, never())
                .generate(any(MessageRequest.class));

        assertEquals(
                "ai",
                redirectAttributes.getFlashAttributes()
                        .get("selectedGenerationMethod")
        );
    }

    /**
     * 日報の作業内容が未入力の場合、
     * エラーが表示され、文章生成が行われないこと
     */
    @Test
    void generateShowsErrorWhenReportContentIsEmpty() {

        MessageService messageService =
                mock(MessageService.class);

        NewsService newsService =
                mock(NewsService.class);

        AiMessageService aiMessageService =
                mock(AiMessageService.class);

        HomeController controller = new HomeController(
                messageService,
                newsService,
                aiMessageService
        );

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("");
        form.setTone(Tone.NORMAL);
        form.setGenerationMethod("template");

        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        String viewName = controller.generate(
                form,
                redirectAttributes
        );

        assertEquals(
                "redirect:/",
                viewName
        );

        assertEquals(
                "今日やったことを入力してください。",
                redirectAttributes.getFlashAttributes().get("errorMessage")
        );

        verify(messageService, never())
                .generate(any(MessageRequest.class));

        verify(aiMessageService, never())
                .generate(any(MessageRequest.class));

        assertEquals(
                MessageType.REPORT,
                redirectAttributes.getFlashAttributes().get("selectedType")
        );

        assertEquals(
                "",
                redirectAttributes.getFlashAttributes().get("workContent")
        );

        assertEquals(
                Tone.NORMAL,
                redirectAttributes.getFlashAttributes().get("selectedTone")
        );

        assertEquals(
                "template",
                redirectAttributes.getFlashAttributes()
                        .get("selectedGenerationMethod")
        );
    }

    /**
     * 不正な生成方法が指定された場合、
     * エラーが表示され、文章生成が行われないこと
     */
    @Test
    void generateRejectsInvalidGenerationMethod() {

        MessageService messageService =
                mock(MessageService.class);

        NewsService newsService =
                mock(NewsService.class);

        AiMessageService aiMessageService =
                mock(AiMessageService.class);

        HomeController controller = new HomeController(
                messageService,
                newsService,
                aiMessageService
        );

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("Javaを学習した");
        form.setTone(Tone.NORMAL);
        form.setGenerationMethod("invalid");

        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        String viewName = controller.generate(
                form,
                redirectAttributes
        );

        assertEquals(
                "redirect:/",
                viewName
        );

        assertEquals(
                "生成方法が不正です。",
                redirectAttributes.getFlashAttributes().get("errorMessage")
        );

        assertFalse(
                redirectAttributes.getFlashAttributes()
                        .containsKey("message")
        );

        verify(messageService, never())
                .generate(any(MessageRequest.class));

        verify(aiMessageService, never())
                .generate(any(MessageRequest.class));
    }
}
