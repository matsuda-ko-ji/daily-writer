package com.example.dailywriter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;

class HomeControllerTest {

    @Test
    void generateRedirectsToHomeWhenInputIsValid() {

        // ① Serviceのモックを作成
        MessageService messageService = mock(MessageService.class);

        // ② Controllerを作成
        NewsService newsService = mock(NewsService.class);

        HomeController controller = new HomeController(messageService, newsService);

        // ③ フォームの入力値を設定
        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("Javaを学習した");
        form.setTone(Tone.NORMAL);

        // ④ Serviceの戻り値を設定
        when(messageService.generate(any(MessageRequest.class)))
                .thenReturn("テスト用の文章");

        // ⑤ RedirectAttributesを用意
        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        // ⑥ Controllerを実行
        String viewName = controller.generate(
                form,
                redirectAttributes
        );

        // ⑦ リダイレクト先を確認
        assertEquals(
                "redirect:/",
                viewName
        );

        // ⑧ 画面に渡す文章を確認
        assertEquals(
                "テスト用の文章",
                redirectAttributes.getFlashAttributes().get("message")
        );

        // ⑨ Serviceが呼び出されたことを確認
        verify(messageService).generate(any(MessageRequest.class));
    }

    @Test
    void generateShowsErrorWhenReportContentIsEmpty() {

        MessageService messageService = mock(MessageService.class);

        NewsService newsService = mock(NewsService.class);

        HomeController controller = new HomeController(messageService, newsService);

        MessageForm form = new MessageForm();

        form.setType(MessageType.REPORT);
        form.setWorkContent("");
        form.setTone(Tone.NORMAL);

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
    }
}