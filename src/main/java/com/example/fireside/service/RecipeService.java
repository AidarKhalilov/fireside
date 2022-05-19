package com.example.fireside.service;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import com.example.fireside.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final List<Recipe> currentRecipes = new ArrayList<>();

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public boolean saveRecipe(User user, Recipe recipe) {
        List<Recipe> recipes = recipeRepository.findByAuthor(user);
        for (Recipe single : recipes) {
            if (single.getTitle().equals(recipe.getTitle())) {
                return false;
            }
        }
        recipeRepository.save(recipe);
        return true;
    }

    public List<Recipe> findByCategory(User author, String category) {
        List<Recipe> recipes = recipeRepository.findByAuthor(author);
        List<Recipe> result = new ArrayList<>();
        for (Recipe element : recipes) {
            if (element.getCategory().toLowerCase().contains(category.toLowerCase())) {
                result.add(element);
            }
        }
        return result;
    }

    public void saveRecipeList(List<Recipe> recipes) {
        if (!this.currentRecipes.isEmpty()) {
            this.currentRecipes.clear();
        }
        this.currentRecipes.addAll(recipes);
    }

    public List<Recipe> getRecipeList() {
        return this.currentRecipes;
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> getRecipes(User author) {
        List<Recipe> recipes = recipeRepository.findByAuthor(author);
        if (recipes.isEmpty()) {
            return null;
        }
        return recipes;
    }

}
