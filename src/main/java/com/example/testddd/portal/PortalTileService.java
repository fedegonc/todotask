package com.example.testddd.portal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PortalTileService {

    private final PortalTileRepository repository;

    public PortalTileService(PortalTileRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PortalTile> listEnabledTiles() {
        return repository.findAllByEnabledTrueOrderByPositionAsc();
    }

    @Transactional(readOnly = true)
    public List<PortalTile> listAllTiles() {
        return repository.findAllByOrderByPositionAsc();
    }

    @Transactional(readOnly = true)
    public long countTiles() {
        return repository.count();
    }

    public PortalTile createTile(PortalTileForm form, String imageUrl) {
        PortalTile tile = PortalTile.create(form.getTitle(), form.getDestination(), form.getPosition(), imageUrl);
        if (!form.isEnabled()) {
            tile.disable();
        }
        return repository.save(tile);
    }

    public PortalTile updateTile(Long id, PortalTileForm form, String imageUrl) {
        PortalTile tile = getOrThrow(id);
        tile.rename(form.getTitle());
        tile.redirectTo(form.getDestination());
        tile.moveTo(form.getPosition());
        if (imageUrl != null) {
            tile.changeImage(imageUrl);
        }
        if (form.isEnabled()) {
            tile.enable();
        } else {
            tile.disable();
        }
        return repository.save(tile);
    }

    public void deleteTile(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tile no encontrada");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PortalTile getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tile no encontrada"));
    }
}
