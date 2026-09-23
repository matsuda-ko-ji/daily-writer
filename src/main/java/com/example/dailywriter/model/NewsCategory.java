package com.example.dailywriter.model;

/**
 * ニュースのカテゴリ
 */
public enum NewsCategory {

    GENERAL("総合"),
    TECHNOLOGY("IT・テクノロジー"),
    BUSINESS("ビジネス"),
    LIFESTYLE("生活");

    private final String displayName;

    NewsCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}