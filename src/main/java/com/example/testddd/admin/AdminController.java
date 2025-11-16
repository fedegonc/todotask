package com.example.testddd.admin;

import com.example.testddd.card.Card;
import com.example.testddd.card.CardForm;
import com.example.testddd.card.CardRepository;
import com.example.testddd.image.CloudinaryImageService;
import com.example.testddd.portal.PortalTileForm;
import com.example.testddd.portal.PortalTileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final CardRepository cardRepository;
    private final CloudinaryImageService imageService;
    private final PortalTileService portalTileService;

    public AdminController(CardRepository cardRepository,
                           CloudinaryImageService imageService,
                           PortalTileService portalTileService) {
        this.cardRepository = cardRepository;
        this.imageService = imageService;
        this.portalTileService = portalTileService;
    }

    @ModelAttribute("cardForm")
    public CardForm cardForm() {
        return new CardForm();
    }

    @ModelAttribute("portalTileForm")
    public PortalTileForm portalTileForm() {
        return new PortalTileForm();
    }

    @ModelAttribute("editing")
    public boolean editing() {
        return false;
    }

    @ModelAttribute("tileEditing")
    public boolean tileEditing() {
        return false;
    }

    @ModelAttribute("adminNavItems")
    public List<AdminNavItem> adminNavItems() {
        return List.of(
                new AdminNavItem("Servicios", "/admin/servicios", "Gestionar tarjetas de servicios"),
                new AdminNavItem("Portal", "/admin/portal-tiles", "Secciones destacadas del portal")
        );
    }

    @ModelAttribute("activityTrend")
    public List<Integer> activityTrend() {
        return List.of(42, 48, 46, 55, 63, 58, 72);
    }

    @ModelAttribute("cardCount")
    public long cardCount() {
        return cardRepository.count();
    }

    @ModelAttribute("portalTileCount")
    public long portalTileCount() {
        return portalTileService.countTiles();
    }

    @GetMapping
    public String dashboard(Model model) {
        populateDashboardModel(model);
        return "admin/admin";
    }

    @GetMapping("/servicios")
    public String servicesSection(Model model) {
        model.addAttribute("sectionTitle", "Administrar servicios");
        return "admin/services";
    }

    @GetMapping("/portal-tiles")
    public String portalTilesSection(Model model) {
        model.addAttribute("sectionTitle", "Administrar secciones del portal");
        populatePortalTilesModel(model);
        return "admin/portal-tiles";
    }

    @GetMapping("/cards/{id}/editar")
    public String editCard(@PathVariable long id, Model model) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        model.addAttribute("cardForm", CardForm.from(card));
        model.addAttribute("editing", true);
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        return "admin/admin";
    }

    @PostMapping("/cards")
    public String createCard(@Valid @ModelAttribute("cardForm") CardForm form,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            populateDashboardModel(model);
            model.addAttribute("editing", false);
            return "admin/admin";
        }
        String imageUrl = form.getImageUrl();
        try {
            String uploadedUrl = imageService.upload(form.getImageFile());
            if (uploadedUrl != null) {
                imageUrl = uploadedUrl;
            }
        } catch (Exception e) {
            log.error("Error subiendo la imagen para nueva tarjeta", e);
        }
        Card card = new Card(form.getTitle(), form.getDescription(), form.getLink(), imageUrl);
        card.assignToSection(form.getSectionKey());
        cardRepository.save(card);
        return "redirect:/admin/portal-tiles";
    }
    @PutMapping("/cards/{id}")
    public String updateCard(@PathVariable long id,
                             @Valid @ModelAttribute("cardForm") CardForm form,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            form.setId(id);
            populateDashboardModel(model);
            model.addAttribute("editing", true);
            return "admin/admin";
        }

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        card.rename(form.getTitle());
        card.changeDescription(form.getDescription());
        card.changeLink(form.getLink());
        card.assignToSection(form.getSectionKey());

        String imageUrl = form.getImageUrl();
        try {
            String uploadedUrl = imageService.upload(form.getImageFile());
            if (uploadedUrl != null) {
                imageUrl = uploadedUrl;
            }
        } catch (Exception e) {
            log.error("Error subiendo la imagen al actualizar tarjeta {}", id, e);
        }
        card.changeImage(imageUrl);

        cardRepository.save(card);
        return "redirect:/admin/portal-tiles";
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCard(@PathVariable long id) {
        if (!cardRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado");
        }
        cardRepository.deleteById(id);
        return "redirect:/admin/portal-tiles";
    }

    @PostMapping("/portal-tiles")
    public String createPortalTile(@Valid @ModelAttribute("portalTileForm") PortalTileForm form,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            populatePortalTilesModel(model);
            model.addAttribute("tileEditing", false);
            return "admin/portal-tiles";
        }
        String imageUrl = resolveImageUrl(form.getImageUrl(), form.getImageFile());
        portalTileService.createTile(form, imageUrl);
        return "redirect:/admin/portal-tiles";
    }

    @GetMapping("/portal-tiles/{id}/editar")
    public String editPortalTile(@PathVariable long id, Model model) {
        try {
            var tile = portalTileService.getOrThrow(id);
            model.addAttribute("portalTileForm", PortalTileForm.from(tile));
            model.addAttribute("tileEditing", true);
            populatePortalTilesModel(model);
            model.addAttribute("sectionTitle", "Administrar secciones del portal");
            return "admin/portal-tiles";
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sección no encontrada");
        }
    }

    @PutMapping("/portal-tiles/{id}")
    public String updatePortalTile(@PathVariable long id,
                                   @Valid @ModelAttribute("portalTileForm") PortalTileForm form,
                                   BindingResult bindingResult,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            form.setId(id);
            populatePortalTilesModel(model);
            model.addAttribute("tileEditing", true);
            model.addAttribute("sectionTitle", "Administrar secciones del portal");
            return "admin/portal-tiles";
        }

        try {
            String imageUrl = resolveImageUrl(form.getImageUrl(), form.getImageFile());
            portalTileService.updateTile(id, form, imageUrl);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sección no encontrada");
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/portal-tiles/{id}")
    public String deletePortalTile(@PathVariable long id) {
        try {
            portalTileService.deleteTile(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sección no encontrada");
        }
        return "redirect:/admin";
    }

    private void populateDashboardModel(Model model) {
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        model.addAttribute("portalTiles", portalTileService.listAllTiles());
    }

    private void populatePortalTilesModel(Model model) {
        model.addAttribute("portalTiles", portalTileService.listAllTiles());
    }

    public record AdminNavItem(String label, String path, String description) {}

    private String resolveImageUrl(String currentUrl, MultipartFile file) {
        String imageUrl = currentUrl;
        try {
            String uploadedUrl = imageService.upload(file);
            if (uploadedUrl != null) {
                imageUrl = uploadedUrl;
            }
        } catch (Exception e) {
            log.error("Error subiendo la imagen para portal tile", e);
        }
        return imageUrl;
    }
}
