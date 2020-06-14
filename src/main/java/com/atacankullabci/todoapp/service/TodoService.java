package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.Todo;
import com.atacankullabci.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void saveTodo(Todo todo) {
        Todo sameNameTodo = todoRepository.getTodoByTodoName(todo.getTodoName());
        if (sameNameTodo == null) {
            todoRepository.save(todo);
        }
    }
}
