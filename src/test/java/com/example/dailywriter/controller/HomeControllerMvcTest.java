package com.example.dailywriter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.dailywriter.dto.MessageRequest;
import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.MessageType;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.NewsCategory;
import com.example.dailywriter.model.Tone;
import com.example.dailywriter.service.AiMessageService;
import com.example.dailywriter.service.MessageService;
import com.example.dailywriter.service.NewsService;

class HomeControllerMvcTest {

    private MessageService messageService;
    private NewsService newsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        messageService = mock(MessageService.class);
        newsService = mock(NewsService.class);

        AiMessageService aiMessageService =
                mock(AiMessageService.class);

        HomeController controller =
                new HomeController(
                        messageService,
                        newsService,
                        aiMessageService
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void postGenerateRedirectsToHomeWhenInputIsValid() throws Exception {

        when(messageService.generate(any(MessageRequest.class)))
                .thenReturn("テスト用の文章");

        mockMvc.perform(
                post("/generate")
                        .param("type", "REPORT")
                        .param("workContent", "Javaを学習した")
                        .param("tone", "NORMAL")
        )
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"))
        .andExpect(flash().attribute(
                "message",
                "テスト用の文章"
        ));
    }

    @Test
    void postGenerateShowsErrorWhenReportContentIsEmpty() throws Exception {

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
                Tone.NORMAL
        ));

        verify(messageService, never())
                .generate(any(MessageRequest.class));
    }

    @Test
    void getHomeReturnsIndexView() throws Exception {

        mockMvc.perform(
                get("/")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
    }

    @Test
    void getGenerateReturnsMethodNotAllowed() throws Exception {

        mockMvc.perform(
                get("/generate")
        )
        .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void postGeneratePassesCorrectRequestToService() throws Exception {

        // ① Serviceが返す文章を設定する
        when(messageService.generate(any(MessageRequest.class)))
                .thenReturn("テスト用の文章");

        // ② HTTPリクエストを送信する
        mockMvc.perform(
                post("/generate")
                        .param("type", "REPORT")
                        .param("workContent", "  Javaを学習した  ")
                        .param("tone", "POLITE")
        )
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));

        // ③ 引数を取得するためのCaptorを作成する
        ArgumentCaptor<MessageRequest> captor =
                ArgumentCaptor.forClass(MessageRequest.class);

        // ④ Serviceに渡された引数を取得する
        verify(messageService).generate(captor.capture());

        // ⑤ 取得した引数を取り出す
        MessageRequest request = captor.getValue();

        // ⑥ 引数の中身を確認する
        assertEquals(
                MessageType.REPORT,
                request.type()
        );

        assertEquals(
                "Javaを学習した",
                request.workContent()
        );

        assertEquals(
                Tone.POLITE,
                request.tone()
        );
    }

    @Test
    void getIndexAddsNewsListToModel() throws Exception {

        News news = new News(
                "テストニュース",
                "テスト概要",
                "https://example.com",
                NewsCategory.TECHNOLOGY,
                "テストニュース"
        );

        List<News> newsList =
                List.of(news);

        when(newsService.getLatestNews())
                .thenReturn(newsList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(
                        model().attribute(
                                "newsList",
                                newsList
                        )
                );
    }

    @Test
    void getIndexDisplaysPageWhenNewsFetchFails() throws Exception {

        // ニュース取得失敗を再現
        when(newsService.getLatestNews())
                .thenThrow(
                        new NewsFetchException("通信エラー")
                );

        // トップ画面へアクセス
        mockMvc.perform(get("/"))

                // 画面は正常に表示される
                .andExpect(status().isOk())

                // index.htmlを表示する
                .andExpect(view().name("index"))

                // ニュース一覧は空になる
                .andExpect(
                        model().attribute(
                                "newsList",
                                List.of()
                        )
                )

                // エラーメッセージが設定される
                .andExpect(
                        model().attribute(
                                "newsError",
                                "ニュースを取得できませんでした。時間をおいて再度お試しください。"
                        )
                );
    }

    @Test
    void getIndexDisplaysPageWhenNewsParsingFails() throws Exception {

        when(newsService.getLatestNews())
                .thenThrow(
                        new NewsFetchException("XML解析エラー")
                );

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(
                        model().attribute(
                                "newsList",
                                List.of()
                        )
                )
                .andExpect(
                        model().attributeExists("newsError")
                );
    }
}