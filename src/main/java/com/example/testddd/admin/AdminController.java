package com.example.testddd.admin;

import com.example.testddd.card.Card;
import com.example.testddd.card.CardForm;
import com.example.testddd.card.CardRepository;
import jakarta.validation.Valid;
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

    private final CardRepository cardRepository;

    public AdminController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
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
        Card card = new Card(form.getTitle(), form.getDescription(), form.getLink());
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
        card.setTitle(form.getTitle());
        card.setDescription(form.getDescription());
        card.setLink(form.getLink());
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
