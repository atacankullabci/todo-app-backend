package com.atacankullabci.todoapp.repository;


import com.atacankullabci.todoapp.common.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, Long> {
    List<Item> getAllByTodoRefId(Long id);

    Item getItemById(Long id);

}
