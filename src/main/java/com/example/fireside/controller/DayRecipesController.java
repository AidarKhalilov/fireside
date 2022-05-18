package com.example.fireside.controller;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import com.example.fireside.service.RecipeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class DayRecipesController {

    private final List<Recipe> recipes = new ArrayList<>();

    private final RecipeService recipeService;

    public DayRecipesController (RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void parseRecipe(User user) throws IOException {
        String URL = "https://www.iamcook.ru/new/yesterday";
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
    public String getRecipes(@AuthenticationPrincipal User user, Model model) {
        if (!recipes.isEmpty()) {
            recipes.clear();
        }
        try {
            parseRecipe(user);
        } catch (IOException e) {
            model.addAttribute(e.getMessage());
        }
        if (!recipes.isEmpty()) {
            model.addAttribute("recipes", recipes);
        }
        else {
            model.addAttribute("recipesNotFound", "Не удалось найти рецептов на сегодня!");
        }
        model.addAttribute("user", user);
        return "osnova";
    }

    @PostMapping("/recipes")
    public String addRecipes(@RequestParam String title, @RequestParam String description,
                             @RequestParam String category, @RequestParam String image,
                             @AuthenticationPrincipal User author, Model model) {
        Recipe recipe = new Recipe(title, description, category, image, author);
        if (!recipeService.saveRecipe(author, recipe)) {
            model.addAttribute("recipeError", "Этот рецепт уже находится в вашей корзине!");
        }
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/my")
    public String getMyRecipes(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> myRecipes = recipeService.getRecipes(user);
        if (myRecipes == null) {
            model.addAttribute("user", user);
            model.addAttribute("recipesNotFound", "Вы, сир, долбоеб, ничего не выбрали!");
            return "biblioteka";
        }
        model.addAttribute("user", user);
        model.addAttribute("recipes", myRecipes);
        return "biblioteka";
    }

    @DeleteMapping("/recipes/my/{id}")
    public String deleteRecipe(@PathVariable(name = "id") Long id) {
        recipeService.deleteRecipe(id);
        return "redirect:/recipes/my";
    }

    @PostMapping("/recipes/my/find")
    public String findRecipes(@RequestParam String category, Model model) {
        List<Recipe> recipes = recipeService.findByCategory(category);
        if (recipes == null) {
            model.addAttribute("recipesNotFound", "Рецептов в данной категории не найдено!");
            return "something";
        }
        model.addAttribute("recipes", recipes);
        return "something";
    }
}
