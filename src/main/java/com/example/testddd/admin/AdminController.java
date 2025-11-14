package com.example.testddd.admin;

import com.example.testddd.card.Card;
import com.example.testddd.card.CardForm;
import com.example.testddd.card.CardRepository;
import com.example.testddd.image.CloudinaryImageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final CardRepository cardRepository;
    private final CloudinaryImageService imageService;

    public AdminController(CardRepository cardRepository, CloudinaryImageService imageService) {
        this.cardRepository = cardRepository;
        this.imageService = imageService;
    }

    @ModelAttribute("cardForm")
    public CardForm cardForm() {
        return new CardForm();
    }

    @ModelAttribute("editing")
    public boolean editing() {
        return false;
    }

    @ModelAttribute("cardCount")
    public long cardCount() {
        return cardRepository.count();
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        return "admin/index";
    }

    @GetMapping("/cards/{id}/editar")
    public String editCard(@PathVariable long id, Model model) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        model.addAttribute("cardForm", CardForm.from(card));
        model.addAttribute("editing", true);
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        return "admin/index";
    }

    @PostMapping("/cards")
    public String createCard(@Valid @ModelAttribute("cardForm") CardForm form,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
            model.addAttribute("editing", false);
            return "admin/index";
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
        cardRepository.save(card);
        return "redirect:/admin";
    }
    @PutMapping("/cards/{id}")
    public String updateCard(@PathVariable long id,
                             @Valid @ModelAttribute("cardForm") CardForm form,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            form.setId(id);
            model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
            model.addAttribute("editing", true);
            return "admin/index";
        }

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        card.rename(form.getTitle());
        card.changeDescription(form.getDescription());
        card.changeLink(form.getLink());

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
        return "redirect:/admin";
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCard(@PathVariable long id) {
        if (!cardRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado");
        }
        cardRepository.deleteById(id);
        return "redirect:/admin";
    }
}
