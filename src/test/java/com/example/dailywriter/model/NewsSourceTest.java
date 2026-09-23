package com.example.dailywriter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NewsSourceTest {

    @Test
    void newsSourceHoldsNameUrlAndCategory() {
        NewsSource source = new NewsSource(
                "Hacker News",
                "https://hnrss.org/frontpage",
                NewsCategory.TECHNOLOGY
        );

        assertEquals("Hacker News", source.name());
        assertEquals("https://hnrss.org/frontpage", source.url());
        assertEquals(NewsCategory.TECHNOLOGY, source.category());
    }
}
