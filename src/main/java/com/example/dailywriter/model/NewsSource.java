package com.example.dailywriter.model;

/**
 * ニュース取得元
 *
 * @param name     取得元の名前
 * @param url      RSSのURL
 * @param category ニュースカテゴリ
 */
public record NewsSource(
        String name,
        String url,
        NewsCategory category
) {
}
