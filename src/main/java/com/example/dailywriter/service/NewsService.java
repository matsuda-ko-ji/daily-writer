package com.example.dailywriter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.NewsCategory;

@Service
public class NewsService {

    private static final String NEWS_URL =
            "https://hnrss.org/frontpage";

    private final RestClient restClient;
    private final NewsXmlParser newsXmlParser;

    public NewsService(
            RestClient.Builder restClientBuilder,
            NewsXmlParser newsXmlParser
    ) {
        this.restClient = restClientBuilder.build();
        this.newsXmlParser = newsXmlParser;
    }

    /**
     * 最新ニュースを取得する
     *
     * @return ニュース一覧
     */
    public List<News> getLatestNews() {

        try {

            String xml = restClient.get()
                    .uri(NEWS_URL)
                    .retrieve()
                    .body(String.class);

            if (xml == null || xml.isBlank()) {
                throw new NewsFetchException(
                        "ニュースの取得結果が空です。"
                );
            }

            return newsXmlParser.parse(
                    xml,
                    NewsCategory.TECHNOLOGY
            );

        } catch (RestClientException e) {

            throw new NewsFetchException(
                    "ニュースのHTTP通信に失敗しました。",
                    e
            );
        }
    }
}