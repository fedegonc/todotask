package com.example.testddd.card;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/seccion")
public class SectionController {

    private final CardRepository cardRepository;

    public SectionController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/{key}")
    public String listBySection(@PathVariable String key, Model model) {
        List<Card> services = cardRepository.findAllBySectionKeyOrderByTitleAsc(key);

        model.addAttribute("services", services);
        model.addAttribute("sectionKey", key);

        String sectionTitle = "Servicios";
        if (key != null && !key.isBlank()) {
            String normalized = key.trim().replace('-', ' ');
            if (!normalized.isEmpty()) {
                sectionTitle = normalized.substring(0, 1).toUpperCase() + normalized.substring(1);
            }
        }
        model.addAttribute("sectionTitle", sectionTitle);

        return "service/list";
    }
}
