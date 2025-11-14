package com.example.testddd.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CardForm {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @Size(max = 512, message = "La descripción debe tener hasta 512 caracteres")
    private String description;

    @NotBlank(message = "El enlace es obligatorio")
    private String link;

    public CardForm() {
    }

    public CardForm(Long id, String title, String description, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public static CardForm from(Card card) {
        return new CardForm(card.getId(), card.getTitle(), card.getDescription(), card.getLink());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
