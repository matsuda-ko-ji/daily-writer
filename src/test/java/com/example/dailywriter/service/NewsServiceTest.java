package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.example.dailywriter.model.News;

class NewsServiceTest {

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

        List<News> newsList =
                newsService.getLatestNews();

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

    @Test
    void getLatestNewsRejectsEmptyResponse() {

        NewsService newsService = createNewsService("");

        assertThrows(
                IllegalStateException.class,
                newsService::getLatestNews
        );
    }

    @Test
    void getLatestNewsRejectsInvalidXml() {

        NewsService newsService =
                createNewsService("<rss><item>");

        assertThrows(
                IllegalStateException.class,
                newsService::getLatestNews
        );
    }

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

        when(restClient.get()).thenReturn(
                (RestClient.RequestHeadersUriSpec) uriSpec
        );

        doReturn(headersSpec)
        .when(uriSpec)
        .uri("https://hnrss.org/frontpage");

        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(String.class))
                .thenReturn(responseXml);

        return new NewsService(builder);
    }
}