package com.example.dailywriter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dailywriter.model.News;

@Service
public class NewsService {

    public List<News> getLatestNews() {

        return List.of(
                new News(
                        "Javaの新機能に関するニュース",
                        "Javaの最新機能や開発手法について紹介します。",
                        "https://example.com/java"
                ),
                new News(
                        "AWSのクラウド技術に関するニュース",
                        "クラウドサービスの活用方法について紹介します。",
                        "https://example.com/aws"
                )
        );
    }
}