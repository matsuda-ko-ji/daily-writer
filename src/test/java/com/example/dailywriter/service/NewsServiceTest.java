package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;
import com.example.dailywriter.model.NewsCategory;
import com.example.dailywriter.model.NewsSource;

class NewsServiceTest {

    /**
     * 正常なXMLからニュース一覧を取得できること
     */
    @Test
    void getLatestNewsReturnsNewsFromXml() {

        String xml = """
                <rss version="2.0">
                    <channel>
                        <item>
                            <title>Javaのニュース</title>
                            <description>Javaの記事です。</description>
                            <link>https://example.com/java</link>
                        </item>
                        <item>
                            <title>AWSのニュース</title>
                            <description>AWSの記事です。</description>
                            <link>https://example.com/aws</link>
                        </item>
                    </channel>
                </rss>
                """;

        NewsService newsService = createNewsService(xml);

        List<News> newsList = newsService.getLatestNews();

        assertEquals(2, newsList.size());

        assertEquals(
                "Javaのニュース",
                newsList.get(0).title()
        );

        assertEquals(
                "Javaの記事です。",
                newsList.get(0).description()
        );

        assertEquals(
                "https://example.com/java",
                newsList.get(0).url()
        );
    }

    /**
     * レスポンスが空の場合、独自例外が発生すること
     */
    @Test
    void getLatestNewsRejectsEmptyResponse() {

        NewsService newsService = createNewsService("");

        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                newsService::getLatestNews
        );

        assertEquals(
                "ニュースの取得結果が空です。",
                exception.getMessage()
        );
    }

    /**
     * 不正なXMLの場合、独自例外が発生すること
     */
    @Test
    void getLatestNewsRejectsInvalidXml() {

        NewsService newsService =
                createNewsService("<rss><item>");

        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                newsService::getLatestNews
        );

        assertEquals(
                "ニュースXMLの解析に失敗しました。",
                exception.getMessage()
        );
    }

    /**
     * HTTP通信エラーが独自例外に変換され、
     * 元の例外が保持されること
     */
    @Test
    void getLatestNewsWrapsHttpException() {

        // 元のHTTP通信エラー
        RestClientException cause =
                new RestClientException("通信エラー");

        // モックを準備
        RestClient.Builder builder =
                mock(RestClient.Builder.class);

        RestClient restClient =
                mock(RestClient.class);

        RestClient.RequestHeadersUriSpec<?> uriSpec =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec<?> headersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec responseSpec =
                mock(RestClient.ResponseSpec.class);

        when(builder.build()).thenReturn(restClient);

        doReturn(uriSpec)
                .when(restClient)
                .get();

        doReturn(headersSpec)
                .when(uriSpec)
                .uri("https://hnrss.org/frontpage");

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        // レスポンス取得時にHTTP通信エラーを発生させる
        when(responseSpec.body(String.class))
                .thenThrow(cause);

        NewsService newsService = new NewsService(
                builder,
                new NewsXmlParser(),
                createNewsSourceProvider()
        );

        // 独自例外が発生すること
        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                newsService::getLatestNews
        );

        // 独自例外のメッセージを確認
        assertEquals(
                "ニュースのHTTP通信に失敗しました。",
                exception.getMessage()
        );

        // 元の例外が保持されていること
        assertSame(
                cause,
                exception.getCause()
        );
    }

    /**
     * 指定したXMLを返すNewsServiceを作成する
     */
    private NewsService createNewsService(String responseXml) {

        RestClient.Builder builder =
                mock(RestClient.Builder.class);

        RestClient restClient =
                mock(RestClient.class);

        RestClient.RequestHeadersUriSpec<?> uriSpec =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec<?> headersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec responseSpec =
                mock(RestClient.ResponseSpec.class);

        when(builder.build()).thenReturn(restClient);

        doReturn(uriSpec)
                .when(restClient)
                .get();

        doReturn(headersSpec)
                .when(uriSpec)
                .uri("https://hnrss.org/frontpage");

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(String.class))
                .thenReturn(responseXml);

        return new NewsService(
                builder,
                new NewsXmlParser(),
                createNewsSourceProvider()
        );
    }

    /**
     * ニュースが6件以上ある場合、先頭5件のみ取得すること
     */
    @Test
    void getLatestNewsReturnsAtMostFiveNews() {

    String xml = """
            <rss version="2.0">
                    <channel>
                    <item><title>ニュース1</title></item>
                    <item><title>ニュース2</title></item>
                    <item><title>ニュース3</title></item>
                    <item><title>ニュース4</title></item>
                    <item><title>ニュース5</title></item>
                    <item><title>ニュース6</title></item>
                    </channel>
            </rss>
            """;

    NewsService newsService = createNewsService(xml);

    List<News> newsList = newsService.getLatestNews();

    assertEquals(5, newsList.size());

    assertEquals("ニュース1", newsList.get(0).title());
    assertEquals("ニュース5", newsList.get(4).title());
    }

    /**
     * ニュースが0件の場合、空のリストを返すこと
     */
    @Test
    void getLatestNewsReturnsEmptyListWhenNoItems() {

        String xml = """
                <rss>
                    <channel></channel>
                </rss>
                """;

        NewsService newsService = createNewsService(xml);

        List<News> newsList = newsService.getLatestNews();

        assertEquals(0, newsList.size());
    }

    /**
     * ニュースが3件の場合、3件すべて取得すること
     */
    @Test
    void getLatestNewsReturnsAllThreeNews() {

        String xml = """
                <rss>
                    <channel>
                        <item><title>ニュース1</title></item>
                        <item><title>ニュース2</title></item>
                        <item><title>ニュース3</title></item>
                    </channel>
                </rss>
                """;

        NewsService newsService = createNewsService(xml);

        List<News> newsList = newsService.getLatestNews();

        assertEquals(3, newsList.size());
        assertEquals("ニュース3", newsList.get(2).title());
    }

    /**
     * ニュースがちょうど5件の場合、5件すべて取得すること
     */
    @Test
    void getLatestNewsReturnsExactlyFiveNews() {

        String xml = """
                <rss>
                    <channel>
                        <item><title>ニュース1</title></item>
                        <item><title>ニュース2</title></item>
                        <item><title>ニュース3</title></item>
                        <item><title>ニュース4</title></item>
                        <item><title>ニュース5</title></item>
                    </channel>
                </rss>
                """;

        NewsService newsService = createNewsService(xml);

        List<News> newsList = newsService.getLatestNews();

        assertEquals(5, newsList.size());
        assertEquals("ニュース5", newsList.get(4).title());
    }

    /**
     * 指定したニュース取得元のURLとカテゴリが使用されること
     */
    @Test
    void getNewsUsesSpecifiedSource() {

        String xml = """
                <rss version="2.0">
                        <channel>
                        <item>
                                <title>ビジネスニュース</title>
                                <description>ビジネスの記事です。</description>
                                <link>https://example.com/business/article</link>
                        </item>
                        </channel>
                </rss>
                """;

        String sourceUrl =
                "https://example.com/business/rss";

        NewsSource source = new NewsSource(
                "テストビジネスニュース",
                sourceUrl,
                NewsCategory.BUSINESS
        );

        RestClient.Builder builder =
                mock(RestClient.Builder.class);

        RestClient restClient =
                mock(RestClient.class);

        RestClient.RequestHeadersUriSpec<?> uriSpec =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec<?> headersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec responseSpec =
                mock(RestClient.ResponseSpec.class);

        when(builder.build()).thenReturn(restClient);

        doReturn(uriSpec)
                .when(restClient)
                .get();

        doReturn(headersSpec)
                .when(uriSpec)
                .uri(sourceUrl);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(String.class))
                .thenReturn(xml);

        NewsService newsService =
                new NewsService(
                        builder,
                        new NewsXmlParser(),
                        createNewsSourceProvider()
                );

        List<News> newsList =
                newsService.getNews(source);

        assertEquals(1, newsList.size());

        News news = newsList.get(0);

        assertEquals(
                "ビジネスニュース",
                news.title()
        );

        assertEquals(
                NewsCategory.BUSINESS,
                news.category()
        );
    }

    /**
     * テスト用のNewsSourceProviderを作成する
     */
    private NewsSourceProvider createNewsSourceProvider() {

        NewsSourceProvider provider =
                mock(NewsSourceProvider.class);

        NewsSource source = new NewsSource(
                "Hacker News",
                "https://hnrss.org/frontpage",
                NewsCategory.TECHNOLOGY
        );

        when(provider.getSources())
                .thenReturn(List.of(source));

        return provider;
    }

    /**
     * 複数のニュース取得元からニュースを取得できること
     */
    @Test
    void getLatestNewsReturnsNewsFromMultipleSources() {

        String technologyXml = """
                <rss version="2.0">
                    <channel>
                        <item>
                            <title>ITニュース</title>
                        </item>
                    </channel>
                </rss>
                """;

        String businessXml = """
                <rss version="2.0">
                    <channel>
                        <item>
                            <title>ビジネスニュース</title>
                        </item>
                    </channel>
                </rss>
                """;

        NewsSource technologySource = new NewsSource(
                "テストITニュース",
                "https://example.com/technology/rss",
                NewsCategory.TECHNOLOGY
        );

        NewsSource businessSource = new NewsSource(
                "テストビジネスニュース",
                "https://example.com/business/rss",
                NewsCategory.BUSINESS
        );

        RestClient.Builder builder =
                mock(RestClient.Builder.class);

        RestClient restClient =
                mock(RestClient.class);

        RestClient.RequestHeadersUriSpec<?> uriSpec =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec<?> technologyHeadersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.RequestHeadersSpec<?> businessHeadersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec technologyResponseSpec =
                mock(RestClient.ResponseSpec.class);

        RestClient.ResponseSpec businessResponseSpec =
                mock(RestClient.ResponseSpec.class);

        when(builder.build())
                .thenReturn(restClient);

        doReturn(uriSpec)
                .when(restClient)
                .get();

        doReturn(technologyHeadersSpec)
                .when(uriSpec)
                .uri(technologySource.url());

        doReturn(businessHeadersSpec)
                .when(uriSpec)
                .uri(businessSource.url());

        when(technologyHeadersSpec.retrieve())
                .thenReturn(technologyResponseSpec);

        when(businessHeadersSpec.retrieve())
                .thenReturn(businessResponseSpec);

        when(technologyResponseSpec.body(String.class))
                .thenReturn(technologyXml);

        when(businessResponseSpec.body(String.class))
                .thenReturn(businessXml);

        NewsSourceProvider provider =
                mock(NewsSourceProvider.class);

        when(provider.getSources())
                .thenReturn(
                        List.of(
                                technologySource,
                                businessSource
                        )
                );

        NewsService newsService = new NewsService(
                builder,
                new NewsXmlParser(),
                provider
        );

        List<News> newsList =
                newsService.getLatestNews();

        assertEquals(2, newsList.size());

        assertEquals(
                "ITニュース",
                newsList.get(0).title()
        );

        assertEquals(
                NewsCategory.TECHNOLOGY,
                newsList.get(0).category()
        );

        assertEquals(
                "ビジネスニュース",
                newsList.get(1).title()
        );

        assertEquals(
                NewsCategory.BUSINESS,
                newsList.get(1).category()
        );
    }

    /**
     * 一部のニュース取得元で取得に失敗しても、
     * 他の取得元のニュースを取得できること
     */
    @Test
    void getLatestNewsContinuesWhenOneSourceFails() {

        String successXml = """
                <rss version="2.0">
                    <channel>
                        <item>
                            <title>取得成功ニュース</title>
                        </item>
                    </channel>
                </rss>
                """;

        NewsSource failedSource = new NewsSource(
                "取得失敗ニュース",
                "https://example.com/failed/rss",
                NewsCategory.TECHNOLOGY
        );

        NewsSource successSource = new NewsSource(
                "取得成功ニュース",
                "https://example.com/success/rss",
                NewsCategory.BUSINESS
        );

        RestClient.Builder builder =
                mock(RestClient.Builder.class);

        RestClient restClient =
                mock(RestClient.class);

        RestClient.RequestHeadersUriSpec<?> uriSpec =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec<?> failedHeadersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.RequestHeadersSpec<?> successHeadersSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec failedResponseSpec =
                mock(RestClient.ResponseSpec.class);

        RestClient.ResponseSpec successResponseSpec =
                mock(RestClient.ResponseSpec.class);

        when(builder.build())
                .thenReturn(restClient);

        doReturn(uriSpec)
                .when(restClient)
                .get();

        doReturn(failedHeadersSpec)
                .when(uriSpec)
                .uri(failedSource.url());

        doReturn(successHeadersSpec)
                .when(uriSpec)
                .uri(successSource.url());

        when(failedHeadersSpec.retrieve())
                .thenReturn(failedResponseSpec);

        when(successHeadersSpec.retrieve())
                .thenReturn(successResponseSpec);

        when(failedResponseSpec.body(String.class))
                .thenThrow(
                        new RestClientException("通信エラー")
                );

        when(successResponseSpec.body(String.class))
                .thenReturn(successXml);

        NewsSourceProvider provider =
                mock(NewsSourceProvider.class);

        when(provider.getSources())
                .thenReturn(
                        List.of(
                                failedSource,
                                successSource
                        )
                );

        NewsService newsService = new NewsService(
                builder,
                new NewsXmlParser(),
                provider
        );

        List<News> newsList =
                newsService.getLatestNews();

        assertEquals(1, newsList.size());

        assertEquals(
                "取得成功ニュース",
                newsList.get(0).title()
        );

        assertEquals(
                NewsCategory.BUSINESS,
                newsList.get(0).category()
        );
    }
}