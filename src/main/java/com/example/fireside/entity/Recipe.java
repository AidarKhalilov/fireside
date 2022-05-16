package com.example.fireside.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "description", length = 4096)
    private String description;

    private String category;

    private String image;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User author;

    public Recipe() {}

    public Recipe(String title, String description, String category, String image, User author) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.image = image;
        this.author = author;
    }

}
