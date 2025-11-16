package com.example.testddd.portal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "portal_tiles")
public class PortalTile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 72)
    private String title;

    @Column(nullable = false, unique = true, length = 128)
    private String destination;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false)
    private boolean enabled;

    @Column(length = 512)
    private String imageUrl;

    protected PortalTile() {
    }

    private PortalTile(String title, String destination, int position, String imageUrl) {
        this.enabled = true;
        rename(title);
        redirectTo(destination);
        moveTo(position);
        changeImage(imageUrl);
    }

    private PortalTile(String title, String destination, int position) {
        this(title, destination, position, null);
    }

    public static PortalTile create(String title, String destination, int position, String imageUrl) {
        return new PortalTile(title, destination, position, imageUrl);
    }

    public static PortalTile create(String title, String destination, int position) {
        return new PortalTile(title, destination, position);
    }

    public void rename(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (newTitle.length() > 72) {
            throw new IllegalArgumentException("El título es demasiado largo");
        }
        this.title = newTitle.trim();
    }

    public void redirectTo(String newDestination) {
        if (newDestination == null || newDestination.isBlank()) {
            throw new IllegalArgumentException("El destino no puede estar vacío");
        }

        String trimmed = newDestination.trim();
        boolean isRelative = trimmed.startsWith("/");
        boolean isAbsolute = trimmed.startsWith("http://") || trimmed.startsWith("https://");

        if (!isRelative && !isAbsolute) {
            throw new IllegalArgumentException("El destino debe ser una URL absoluta o empezar con /");
        }
        this.destination = trimmed;
    }

    public void moveTo(int newPosition) {
        if (newPosition < 0) {
            throw new IllegalArgumentException("La posición no puede ser negativa");
        }
        this.position = newPosition;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void changeImage(String newImageUrl) {
        if (newImageUrl == null || newImageUrl.isBlank()) {
            this.imageUrl = null;
            return;
        }
        String trimmed = newImageUrl.trim();
        if (!(trimmed.startsWith("http://") || trimmed.startsWith("https://"))) {
            throw new IllegalArgumentException("La imagen debe ser una URL válida");
        }
        if (trimmed.length() > 512) {
            throw new IllegalArgumentException("La URL de la imagen es demasiado larga");
        }
        this.imageUrl = trimmed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public int getPosition() {
        return position;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public record Title(String value) {
    public Title {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("El título no puede estar vacío");
        if (value.length() > 72)
            throw new IllegalArgumentException("Longitud inválida");
    }

    @Override public String toString() { return value; }
}
}
