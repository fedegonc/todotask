package com.example.testddd.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoRestController {

    private final TodoTaskRepository repository;

    public TodoRestController(TodoTaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TodoTask> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public TodoTask create(@RequestBody TodoTask task) {
        return repository.save(task);
    }
}
