package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.model.News;

class NewsServiceTest {

    private final NewsService newsService = new NewsService();

    @Test
    void getLatestNewsReturnsTwoNewsItems() {

        List<News> newsList = newsService.getLatestNews();

        assertEquals(2, newsList.size());
    }

    @Test
    void getLatestNewsReturnsExpectedFirstNews() {

        List<News> newsList = newsService.getLatestNews();

        News firstNews = newsList.get(0);

        assertEquals(
                "Javaの新機能に関するニュース",
                firstNews.title()
        );

        assertEquals(
                "Javaの最新機能や開発手法について紹介します。",
                firstNews.description()
        );

        assertEquals(
                "https://example.com/java",
                firstNews.url()
        );
    }

    @Test
    void getLatestNewsReturnsUnmodifiableList() {

        List<News> newsList = newsService.getLatestNews();

        assertThrows(
                UnsupportedOperationException.class,
                () -> newsList.add(
                        new News(
                                "追加ニュース",
                                "概要",
                                "https://example.com"
                        )
                )
        );
    }
}