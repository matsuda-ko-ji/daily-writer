package com.example.dailywriter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.service.MessageService;

class HomeControllerMvcTest {

    @Test
    void postGenerateRedirectsToHomeWhenInputIsValid() throws Exception {

        // ① Serviceのモックを作成
        MessageService messageService = mock(MessageService.class);

        // ② Controllerを作成
        HomeController controller = new HomeController(messageService);

        // ③ MockMvcを作成
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        // ④ Serviceの戻り値を設定
        when(messageService.generate(any(MessageRequest.class)))
                .thenReturn("テスト用の文章");

        // ⑤ HTTPリクエストを送信
        mockMvc.perform(
                post("/generate")
                        .param("type", "REPORT")
                        .param("workContent", "Javaを学習した")
                        .param("tone", "NORMAL")
        )

        // ⑥ HTTPステータスを確認
        .andExpect(status().is3xxRedirection())

        // ⑦ リダイレクト先を確認
        .andExpect(redirectedUrl("/"))

        // ⑧ Flash Attributeを確認
        .andExpect(flash().attribute(
                "message",
                "テスト用の文章"
        ));
    }

    @Test
    void postGenerateShowsErrorWhenReportContentIsEmpty() throws Exception {

        MessageService messageService = mock(MessageService.class);

        HomeController controller = new HomeController(messageService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mockMvc.perform(
                post("/generate")
                        .param("type", "REPORT")
                        .param("workContent", "")
                        .param("tone", "NORMAL")
        )
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"))
        .andExpect(flash().attribute(
                "errorMessage",
                "今日やったことを入力してください。"
        ))
        .andExpect(flash().attribute(
                "selectedType",
                MessageType.REPORT
        ))
        .andExpect(flash().attribute(
                "workContent",
                ""
        ))
        .andExpect(flash().attribute(
                "selectedTone",
                "NORMAL"
        ));
    }

    @Test
    void getHomeReturnsIndexView() throws Exception {

        MessageService messageService = mock(MessageService.class);

        HomeController controller = new HomeController(messageService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mockMvc.perform(
                get("/")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
    }

    @Test
    void getGenerateReturnsMethodNotAllowed() throws Exception {

        MessageService messageService = mock(MessageService.class);

        HomeController controller = new HomeController(messageService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mockMvc.perform(
                get("/generate")
        )
        .andExpect(status().isMethodNotAllowed());
    }
}