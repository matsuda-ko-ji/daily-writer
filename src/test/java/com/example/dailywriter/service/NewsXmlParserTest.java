package com.example.dailywriter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;

class NewsXmlParserTest {

    private final NewsXmlParser parser = new NewsXmlParser();

    /**
     * 正常なXMLからニュースを取得できること
     */
    @Test
    void parseReturnsNewsFromValidXml() {

        String xml = """
                <rss>
                    <channel>
                        <item>
                            <title>Javaのニュース</title>
                            <description>Javaの記事です。</description>
                            <link>https://example.com/java</link>
                        </item>
                    </channel>
                </rss>
                """;

        List<News> newsList = parser.parse(xml);

        assertEquals(1, newsList.size());
        assertEquals("Javaのニュース", newsList.get(0).title());
        assertEquals("Javaの記事です。", newsList.get(0).description());
        assertEquals("https://example.com/java", newsList.get(0).url());
    }

    /**
     * 不正なXMLの場合、独自例外が発生すること
     */
    @Test
    void parseRejectsInvalidXml() {

        String xml = "<rss><channel>";

        NewsFetchException exception = assertThrows(
                NewsFetchException.class,
                () -> parser.parse(xml)
        );

        assertEquals(
                "ニュースXMLの解析に失敗しました。",
                exception.getMessage()
        );

        assertNotNull(exception.getCause());
    }

    /**
     * ニュースが6件ある場合、先頭5件だけ返すこと
     */
    @Test
    void parseReturnsAtMostFiveNews() {

        String xml = """
                <rss>
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

        List<News> newsList = parser.parse(xml);

        assertEquals(5, newsList.size());
        assertEquals("ニュース1", newsList.get(0).title());
        assertEquals("ニュース5", newsList.get(4).title());
    }

    /**
     * itemが存在しない場合、空のリストを返すこと
     */
    @Test
    void parseReturnsEmptyListWhenNoItems() {

        String xml = """
                <rss>
                    <channel></channel>
                </rss>
                """;

        List<News> newsList = parser.parse(xml);

        assertEquals(0, newsList.size());
    }
}