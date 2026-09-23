package com.example.dailywriter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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

        // 1つでも取得に成功したニュース取得元があるか
        boolean anySourceSucceeded = false;

        for (NewsSource source : newsSourceProvider.getSources()) {

            try {

                List<News> sourceNews =
                        getNews(source);

                anySourceSucceeded = true;

                newsList.addAll(sourceNews);

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

        // すべての取得元で失敗した場合のみ例外を投げる
        if (!anySourceSucceeded && lastException != null) {
            throw lastException;
        }

        return removeDuplicatesByUrl(newsList);
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
                    source
            );

        } catch (RestClientException e) {

            throw new NewsFetchException(
                    "ニュースのHTTP通信に失敗しました。",
                    e
            );
        }
    }

    /**
     * URLが重複しているニュースを除外する
     *
     * URLが設定されていないニュースは
     * 重複判定の対象外とする。
     *
     * @param newsList ニュース一覧
     * @return 重複を除外したニュース一覧
     */
    private List<News> removeDuplicatesByUrl(
            List<News> newsList
    ) {

        Map<String, News> newsByUrl =
                new LinkedHashMap<>();

        List<News> result =
                new ArrayList<>();

        for (News news : newsList) {

            String url = news.url();

            if (url == null || url.isBlank()) {
                result.add(news);
                continue;
            }

            if (!newsByUrl.containsKey(url)) {
                newsByUrl.put(url, news);
                result.add(news);
            }
        }

        return List.copyOf(result);
    }
}