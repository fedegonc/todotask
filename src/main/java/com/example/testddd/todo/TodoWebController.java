package com.example.testddd.todo;

import com.example.testddd.card.CardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoWebController {

    private final TodoTaskRepository repository;
    private final CardRepository cardRepository;

    public TodoWebController(TodoTaskRepository repository, CardRepository cardRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("tasks", repository.findAll());
        model.addAttribute("cards", cardRepository.findAll());
        return "index";
    }

    @PostMapping("/todo")
    public String create(@RequestParam String title, @RequestParam(required = false) String description) {
        repository.save(new TodoTask(title, description));
        return "redirect:/index";
    }
}
