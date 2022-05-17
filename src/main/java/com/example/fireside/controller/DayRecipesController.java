package com.example.fireside.controller;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import com.example.fireside.service.RecipeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DayRecipesController {

    private final RecipeService recipeService;

    public DayRecipesController (RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public String getRecipes(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("recipes", recipeService.getRecipeList());
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
