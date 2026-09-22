package com.example.dailywriter.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;      
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.dailywriter.exception.NewsFetchException;
import com.example.dailywriter.model.News;

@Service
public class NewsService {

    private static final String NEWS_URL =
            "https://hnrss.org/frontpage";

    private static final int MAX_NEWS_COUNT = 5;

    private final RestClient restClient;

    public NewsService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

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

            return parseNews(xml);

        } catch (RestClientException e) {

            throw new NewsFetchException(
                    "ニュースのHTTP通信に失敗しました。",
                    e
            );
        }
    }

    private List<News> parseNews(String xml) {

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // XML外部実体の読み込みを禁止する
            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document = factory
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            NodeList items =
                    document.getElementsByTagName("item");

            List<News> newsList = new ArrayList<>();

            for (int i = 0;
                    i < Math.min(items.getLength(), MAX_NEWS_COUNT);
                    i++) {

                Element item = (Element) items.item(i);

                String title = getText(item, "title");
                String description = getText(item, "description");
                String link = getText(item, "link");

                newsList.add(
                        new News(title, description, link)
                );
            }

            return List.copyOf(newsList);

        } catch (Exception e) {

            throw new NewsFetchException(
                    "ニュースXMLの解析に失敗しました。",
                    e
            );
        }
    }

    private String getText(Element element, String tagName) {

        NodeList nodes =
                element.getElementsByTagName(tagName);

        if (nodes.getLength() == 0) {
            return "";
        }

        return nodes.item(0).getTextContent();
    }
}