package com.example.fireside.component;

import com.example.fireside.entity.Recipe;
import com.example.fireside.service.RecipeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class parseTask {

    @Autowired
    RecipeService recipeService;

    @Scheduled(fixedRate = 3600000)
    public void parseRecipes() {
        try {
            Document document = Jsoup.connect("https://www.iamcook.ru/new/yesterday")
                    .userAgent("Google Chrome")
                    .timeout(5000)
                    .referrer("https://google.com")
                    .get();
            Elements result = document.select("body > table.midcontainer > tbody > tr > td > div#new" +
                    " > div.recblock");
            List<Recipe> recipes = new ArrayList<>();
            List<String> titles = result.select(" > div.info > div.header > a").eachText();
            List<String> titlesCopy = new ArrayList<>(titles);
            if (!recipeService.getRecipeList().isEmpty()) {
                List<String> titlesFromDb = recipeService.getRecipeList()
                        .stream()
                        .map(Recipe::getTitle)
                        .collect(Collectors.toList());
                Collections.sort(titlesCopy);
                Collections.sort(titlesFromDb);
                if (titlesCopy.equals(titlesFromDb)) {
                    return;
                }
            }
            List<String> images = result.select(" > a > img.preimage").eachAttr("src");
            List<String> descriptions = new ArrayList<>();
            List<String> categories = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                Document doc = Jsoup.connect("https://www.iamcook.ru" +
                                result.select(" > div.info > div.header > a").
                                        eachAttr("href").get(i))
                        .userAgent("Google Chrome")
                        .timeout(5000)
                        .referrer("https://google.com")
                        .get();
                descriptions.add(String.join(". ", doc.select("body > table.midcontainer > tbody > tr > " +
                        "td#recipe > table > tbody > tr > td > div#recbody > div.ingredients > div.ilist" +
                        " > div > p").eachText()) + ". " + String.join(" ",doc.select("body > " +
                        "table.midcontainer > tbody > tr > td#recipe > table > tbody > tr > td > div#recbody > " +
                        "div.instructions > div > p").eachText()));
                categories.add(Objects.requireNonNull(doc.select("body > table.midcontainer > tbody > tr > td#recipe > div#path > " +
                        "a.pathlink").last()).text());
            }
            for (int i = 0; i < titles.size(); i++) {
                Recipe recipe = new Recipe(titles.get(i), descriptions.get(i), categories.get(i), images.get(i), null);
                recipes.add(recipe);
            }
            recipeService.saveRecipeList(recipes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
