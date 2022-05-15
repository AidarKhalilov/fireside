package com.example.fireside.controller;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class DayRecipesController {

    private final List<Recipe> recipes = new ArrayList<>();

    public void parseRecipe(User user) throws IOException {
        String URL = "https://www.iamcook.ru/new/today";
        Document document = Jsoup.connect(URL)
                .userAgent("Google Chrome")
                .timeout(5000)
                .referrer("https://google.com")
                .get();
        Elements result = document.select("body > table.midcontainer > tbody > tr > td > div#new" +
                " > div.recblock > div.info > div.header > a");
        Elements pictures = document.select("body > table.midcontainer > tbody > tr > td > div#new" +
                " > div.recblock > a > img.preimage");
        List<String> images = new ArrayList<>(pictures.eachAttr("src"));
        List<String> titles = new ArrayList<>(result.eachText());
        List<String> descriptions = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Document doc = Jsoup.connect("https://www.iamcook.ru" + result.eachAttr("href").get(i))
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
            Recipe recipe = new Recipe(titles.get(i), descriptions.get(i), categories.get(i), images.get(i), user);
            recipes.add(recipe);
        }
    }

    @GetMapping("/recipes")
    public String getRecipes(@AuthenticationPrincipal User user, Model model) throws IOException {
        parseRecipe(user);
        model.addAttribute("recipes", recipes);
        return "something";
    }

}
