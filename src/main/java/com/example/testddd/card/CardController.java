package com.example.testddd.card;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/servicios")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", cardRepository.findAll(Sort.by("title")));
        model.addAttribute("sectionTitle", "Servicios");
        return "service/list";
    }

    @GetMapping("/{slug}")
    public String show(@PathVariable String slug, Model model) {
        String cleanSlug = slug.startsWith("/") ? slug.substring(1) : slug;

        Card card = cardRepository.findByLink(cleanSlug)
                .or(() -> cardRepository.findByLink("/" + cleanSlug))
                .or(() -> cardRepository.findByLink("/servicios/" + cleanSlug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        model.addAttribute("card", card);
        return "service/detail";
    }
}
