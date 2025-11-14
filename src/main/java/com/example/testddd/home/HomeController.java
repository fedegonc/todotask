package com.example.testddd.home;

import com.example.testddd.card.CardRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CardRepository cardRepository;

    public HomeController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("cards", cardRepository.findAll(Sort.by("title")));
        return "index";
    }
}
