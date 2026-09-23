package com.example.dailywriter.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dailywriter.model.NewsCategory;
import com.example.dailywriter.model.NewsSource;

/**
 * ニュース取得元を提供するクラス
 */
@Component
public class NewsSourceProvider {

    /**
     * ニュース取得元一覧を取得する
     *
     * @return ニュース取得元一覧
     */
    public List<NewsSource> getSources() {

        return List.of(
                new NewsSource(
                        "Hacker News",
                        "https://hnrss.org/frontpage",
                        NewsCategory.TECHNOLOGY
                )
        );
    }
}