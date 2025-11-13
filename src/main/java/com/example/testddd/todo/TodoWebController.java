package com.example.testddd.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoWebController {

    private final TodoTaskRepository repository;

    public TodoWebController(TodoTaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("tasks", repository.findAll());
        return "index";
    }

    @PostMapping("/todo")
    public String create(@RequestParam String title, @RequestParam(required = false) String description) {
        repository.save(new TodoTask(title, description));
        return "redirect:/index";
    }
}
