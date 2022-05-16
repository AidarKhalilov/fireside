package com.example.fireside.repository;

import com.example.fireside.entity.Recipe;
import com.example.fireside.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthor(User author);
    List<Recipe> findByCategory(String category);
    Recipe findByTitle(String title);
}
