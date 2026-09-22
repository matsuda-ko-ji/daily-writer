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

        NewsService newsService = new NewsService(builder);

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

        return new NewsService(builder);
    }
}