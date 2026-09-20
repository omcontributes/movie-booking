package com.example.moviebooking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private String genre;
    private String language;
    private int durationMinutes;

    protected Movie() {}

    public Movie(String title, String description, String genre, String language, int durationMinutes) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.language = language;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public int getDurationMinutes() { return durationMinutes; }
}