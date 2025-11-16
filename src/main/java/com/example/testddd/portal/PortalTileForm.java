package com.example.testddd.portal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PortalTileForm {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 72, message = "El título debe tener hasta 72 caracteres")
    private String title;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 128, message = "El destino debe tener hasta 128 caracteres")
    @Pattern(regexp = "(?:/|https?://).+", message = "El destino debe ser una URL absoluta o empezar con /")
    private String destination;

    @Min(value = 0, message = "La posición debe ser mayor o igual a 0")
    private int position;

    private boolean enabled = true;

    @Size(max = 512, message = "La URL de la imagen debe tener hasta 512 caracteres")
    private String imageUrl;

    private transient MultipartFile imageFile;

    public PortalTileForm() {
    }

    public static PortalTileForm from(PortalTile tile) {
        PortalTileForm form = new PortalTileForm();
        form.setId(tile.getId());
        form.setTitle(tile.getTitle());
        form.setDestination(tile.getDestination());
        form.setPosition(tile.getPosition());
        form.setEnabled(tile.isEnabled());
        form.setImageUrl(tile.getImageUrl());
        return form;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
}
