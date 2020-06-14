package com.atacankullabci.todoapp.controller;

import com.atacankullabci.todoapp.common.Todo;
import com.atacankullabci.todoapp.repository.ItemRepository;
import com.atacankullabci.todoapp.repository.TodoRepository;
import com.atacankullabci.todoapp.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200")
public class TodoController {
    private final TodoRepository todoRepository;
    private static final String ENTITY_NAME = "todo";
    private final TodoService todoService;

    public TodoController(TodoRepository todoRepository, TodoService todoService) {
        this.todoRepository = todoRepository;
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        return ResponseEntity.ok(todoRepository.findAll());
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") Long id) {
        Todo todo = todoRepository.getTodoById(id);
        return ResponseEntity.ok().body(todo);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo todo, @PathVariable(name = "id") Long id) throws URISyntaxException {

        Todo currentTodo = todoRepository.getTodoById(id);

        if (currentTodo == null) {
            return new ResponseEntity("Unable to upate. User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        currentTodo.setTodoName(todo.getTodoName());
        currentTodo.setItemList(todo.getItemList());
        currentTodo.setUser(todo.getUser());

        todoRepository.save(currentTodo);
        return new ResponseEntity<>(currentTodo, HttpStatus.OK);
    }

    @PostMapping("/todos")
    public void addTodo(@RequestBody Todo newTodo) {
        todoService.saveTodo(newTodo);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable(name = "id") Long id) {
        todoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
