package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;

class NewsServiceHttpTest {

    @Test
    void getLatestNewsReturnsNewsFromHttpResponse() {

        // ① テスト用のXML
        String xml = """
                <rss version="2.0">
                    <channel>
                        <item>
                            <title>Javaのニュース</title>
                            <description>Javaの記事です。</description>
                            <link>https://example.com/java</link>
                        </item>
                    </channel>
                </rss>
                """;

        // ② 実際のRestClient.Builderを作成
        RestClient.Builder builder = RestClient.builder();

        // ③ HTTP通信を模擬するサーバーを作成
        MockRestServiceServer server =
                MockRestServiceServer.bindTo(builder).build();

        // ④ 期待するリクエストとレスポンスを設定
        server.expect(
                requestTo("https://hnrss.org/frontpage")
        )
        .andExpect(method(HttpMethod.GET))
                .andRespond(
                withSuccess(
                        xml,
                        new MediaType(
                                "application",
                                "xml",
                                StandardCharsets.UTF_8
                        )
                )
        );

        // ⑤ NewsServiceを作成
        NewsService newsService = new NewsService(builder);

        // ⑥ ニュース取得処理を実行
        List<News> newsList = newsService.getLatestNews();

        // ⑦ 取得結果を検証
        assertEquals(1, newsList.size());

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

        // ⑧ 期待したHTTPリクエストが実行されたことを検証
        server.verify();
    }

    /**
     * HTTP 500の場合、独自例外に変換されること
     */
    @Test
    void getLatestNewsWrapsHttp500Error() {

        // ① 実際のRestClient.Builderを作成
        RestClient.Builder builder = RestClient.builder();

        // ② HTTP通信を模擬するサーバーを作成
        MockRestServiceServer server =
                MockRestServiceServer.bindTo(builder).build();

        // ③ HTTP 500を返すように設定
        server.expect(
                requestTo("https://hnrss.org/frontpage")
        )
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

        // ④ NewsServiceを作成
        NewsService newsService = new NewsService(builder);

        // ⑤ 独自例外が発生することを確認
        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                newsService::getLatestNews
        );

        // ⑥ 独自例外のメッセージを確認
        assertEquals(
                "ニュースのHTTP通信に失敗しました。",
                exception.getMessage()
        );

        // ⑦ 元のHTTP例外が保持されていることを確認
        assertNotNull(exception.getCause());

        assertInstanceOf(
                HttpServerErrorException.class,
                exception.getCause()
        );

        // ⑧ 期待したHTTPリクエストが実行されたことを確認
        server.verify();
    }

    /**
     * HTTP 200でもXMLが不正な場合、独自例外が発生すること
     */
    @Test
    void getLatestNewsRejectsInvalidXmlFromHttpResponse() {

        // ① 不正なXMLを用意
        String invalidXml = """
                <rss>
                    <channel>
                        <item>
                """;

        // ② 実際のRestClient.Builderを作成
        RestClient.Builder builder = RestClient.builder();

        // ③ HTTP通信を模擬するサーバーを作成
        MockRestServiceServer server =
                MockRestServiceServer.bindTo(builder).build();

        // ④ HTTP 200で不正なXMLを返す
        server.expect(
                requestTo("https://hnrss.org/frontpage")
        )
        .andExpect(method(HttpMethod.GET))
        .andRespond(
                withSuccess(
                        invalidXml,
                        new MediaType(
                                "application",
                                "xml",
                                StandardCharsets.UTF_8
                        )
                )
        );

        // ⑤ NewsServiceを作成
        NewsService newsService = new NewsService(builder);

        // ⑥ 独自例外が発生することを確認
        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                newsService::getLatestNews
        );

        // ⑦ XML解析エラーのメッセージを確認
        assertEquals(
                "ニュースXMLの解析に失敗しました。",
                exception.getMessage()
        );

        // ⑧ 元の例外が保持されていることを確認
        assertNotNull(exception.getCause());

        // ⑨ 期待したHTTPリクエストが実行されたことを確認
        server.verify();
    }
}