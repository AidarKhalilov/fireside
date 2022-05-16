package com.example.fireside.service;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import com.example.fireside.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public boolean saveRecipe(Recipe recipe) {
        Recipe recipeFromDb = recipeRepository.findByTitle(recipe.getTitle());
        if (recipeFromDb != null) {
            return false;
        }
        recipeRepository.save(recipe);
        return true;
    }

    public List<Recipe> findByCategory(String category) {
        List<Recipe> recipes = recipeRepository.findByCategory(category);
        if (recipes.isEmpty()) {
            return null;
        }
        return recipes;
    }

    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title);
    }

    public List<Recipe> findByAuthor(User author) {
        return recipeRepository.findByAuthor(author);
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
