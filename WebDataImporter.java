package com.example.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebDataImporter {

    public static List<String> importWordsFromWebsite(String url) {
        List<String> wordsList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements wordElements = document.select("table#wordlist tr");

            for (Element wordElement : wordElements) {
                String word = wordElement.text();
                wordsList.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsList;
    }
}
