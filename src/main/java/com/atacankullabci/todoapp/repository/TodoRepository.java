package com.atacankullabci.todoapp.repository;


import com.atacankullabci.todoapp.common.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends MongoRepository<Todo, Long> {

    Todo getTodoById(Long id);

    Todo getTodoByTodoName(String todoName);

}
