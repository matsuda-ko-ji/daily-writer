package com.example.dailywriter.model;

/**
 * ニュース情報
 *
 * @param title       タイトル
 * @param description 概要
 * @param url         記事URL
 * @param category    ニュースカテゴリ
 * @param sourceName  ニュース取得元名
 */
public record News(
        String title,
        String description,
        String url,
        NewsCategory category,
        String sourceName
) {
}