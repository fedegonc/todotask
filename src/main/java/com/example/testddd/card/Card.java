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

    @Column(length = 512)
    private String imageUrl;

    protected Card() {}

    public Card(String title, String description, String link) {
        this(title, description, link, null);
    }

    public Card(String title, String description, String link, String imageUrl) {
        validateTitle(title);
        validateDescription(description);
        validateLink(link);
        validateImageUrl(imageUrl);

        this.title = title;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
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

    public void changeImage(String newImageUrl) {
        validateImageUrl(newImageUrl);
        this.imageUrl = newImageUrl;
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

    private void validateImageUrl(String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (!(value.startsWith("http://") || value.startsWith("https://"))) {
            throw new IllegalArgumentException("La imagen debe ser una URL válida.");
        }
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

    public String getImageUrl() {
        return imageUrl;
    }
}