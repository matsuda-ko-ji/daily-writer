package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.model.NewsCategory;
import com.example.dailywriter.model.NewsSource;

class NewsSourceProviderTest {

    /**
     * ニュース取得元一覧を取得できること
     */
    @Test
    void getSourcesReturnsNewsSources() {

        NewsSourceProvider provider =
                new NewsSourceProvider();

        List<NewsSource> sources =
                provider.getSources();

        assertEquals(1, sources.size());

        NewsSource source = sources.get(0);

        assertEquals(
                "Hacker News",
                source.name()
        );

        assertEquals(
                "https://hnrss.org/frontpage",
                source.url()
        );

        assertEquals(
                NewsCategory.TECHNOLOGY,
                source.category()
        );
    }
}