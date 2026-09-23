package com.example.dailywriter.model;

/**
 * ニュース情報
 *
 * @param title       タイトル
 * @param description 概要
 * @param url         記事URL
 * @param category    ニュースカテゴリ
 */
public record News(
        String title,
        String description,
        String url,
        NewsCategory category
) {
}