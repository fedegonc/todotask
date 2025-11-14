package com.example.testddd.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 512)
    private String description;

    @Column(nullable = false)
    private String link;

    protected Card() {}

    public Card(String title, String description, String link) {
        validateTitle(title);
        validateDescription(description);
        validateLink(link);

        this.title = title;
        this.description = description;
        this.link = link;
    }

    public void rename(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
    }

    public void changeLink(String newLink) {
        validateLink(newLink);
        this.link = newLink;
    }

    public void changeDescription(String newDescription) {
        validateDescription(newDescription);
        this.description = newDescription;
    }

    private void validateTitle(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("El título es demasiado largo.");
        }
    }

    private void validateDescription(String value) {
        if (value != null && value.length() > 512) {
            throw new IllegalArgumentException("La descripción es demasiado larga.");
        }
    }

    private void validateLink(String value) {
    if (value == null || value.isBlank()) {
        throw new IllegalArgumentException("El link no puede estar vacío.");
    }

    // Caso 1: URL completa
    if (value.startsWith("http://") || value.startsWith("https://")) {
        return;
    }

    // Caso 2: slug interno (solo letras, números, guiones y barras)
    if (value.matches("^[a-zA-Z0-9\\-_/]+$")) {
        return;
    }

    throw new IllegalArgumentException("El link debe ser una URL válida o un slug interno.");
}


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}