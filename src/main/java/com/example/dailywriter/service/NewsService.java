package com.example.dailywriter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.NewsSource;

@Service
public class NewsService {

    private static final Logger logger =
            LoggerFactory.getLogger(NewsService.class);
            
    private final RestClient restClient;
    private final NewsXmlParser newsXmlParser;
    private final NewsSourceProvider newsSourceProvider;

    public NewsService(
        RestClient.Builder restClientBuilder,
        NewsXmlParser newsXmlParser,
        NewsSourceProvider newsSourceProvider
    ) {
        this.restClient = restClientBuilder.build();
        this.newsXmlParser = newsXmlParser;
        this.newsSourceProvider = newsSourceProvider;
    }

    /**
     * 最新ニュースを取得する
     *
     * @return ニュース一覧
     */
    public List<News> getLatestNews() {

        List<News> newsList = new ArrayList<>();
        NewsFetchException lastException = null;

        for (NewsSource source : newsSourceProvider.getSources()) {

            try {
                newsList.addAll(getNews(source));
            } catch (NewsFetchException e) {

                logger.warn(
                        "ニュースの取得に失敗しました。source={}, url={}",
                        source.name(),
                        source.url(),
                        e
                );

                lastException = e;
            }
        }

        if (newsList.isEmpty() && lastException != null) {
            throw lastException;
        }

        return List.copyOf(newsList);
    }

    /**
     * 指定したニュース取得元からニュースを取得する
     *
     * @param source ニュース取得元
     * @return ニュース一覧
     */
    public List<News> getNews(NewsSource source) {

        try {

            String xml = restClient.get()
                    .uri(source.url())
                    .retrieve()
                    .body(String.class);

            if (xml == null || xml.isBlank()) {
                throw new NewsFetchException(
                        "ニュースの取得結果が空です。"
                );
            }

            return newsXmlParser.parse(
                    xml,
                    source.category()
            );

        } catch (RestClientException e) {

            throw new NewsFetchException(
                    "ニュースのHTTP通信に失敗しました。",
                    e
            );
        }
    }
}