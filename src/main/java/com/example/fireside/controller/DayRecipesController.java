package com.example.fireside.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class DayRecipesController {

    private final List<String> result = new ArrayList<>();

    public void parseRecipe() throws IOException {
        String URL = "https://www.povarenok.ru/recipes-of-the-day";
        Document document = Jsoup.connect(URL)
                .userAgent("Google Chrome")
                .timeout(5000)
                .referrer("https://google.com")
                .get();
        Elements recipes = document.select("body > div.page-width > div.site-content" +
                " > div.page-bl > div.content-md > section > article.item-bl > h2 > a");
        Elements categories = document.select("body > div.page-width > div.site-content" +
                " > div.page-bl > div.content-md > section > article.item-bl" +
                " > div.article-breadcrumbs");
        result.addAll(recipes.eachText());
        for (Element e : categories) {
            result.add(Objects.requireNonNull(e.select(" > p > span").first()).text());
        }
        for (String s : result) {
            result.set(result.indexOf(s), s.replace("\"", "'"));
        }
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<String>> getRecipes() throws IOException {
        parseRecipe();
        return ResponseEntity.ok(this.result);
    }

}
