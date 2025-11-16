package com.example.testddd.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CardForm {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @Size(max = 512, message = "La descripción debe tener hasta 512 caracteres")
    private String description;

    @NotBlank(message = "El enlace es obligatorio")
    private String link;

    @Size(max = 512, message = "La URL de la imagen debe tener hasta 512 caracteres")
    private String imageUrl;

    private transient MultipartFile imageFile;

    private String sectionKey;

    public CardForm() {
    }

    public CardForm(Long id, String title, String description, String link, String imageUrl, String sectionKey) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
        this.sectionKey = sectionKey;
    }

    public static CardForm from(Card card) {
        return new CardForm(card.getId(), card.getTitle(), card.getDescription(), card.getLink(), card.getImageUrl(), card.getSectionKey());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }
}
