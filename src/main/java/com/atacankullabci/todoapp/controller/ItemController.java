package com.atacankullabci.todoapp.controller;

import com.atacankullabci.todoapp.common.Item;
import com.atacankullabci.todoapp.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok().body(itemRepository.findAll());
    }

    @GetMapping("/items/todo/{id}")
    public ResponseEntity<List<Item>> getAllItemsByTodoId(@PathVariable("id") Long id) {
        List<Item> itemList = itemRepository.getAllByTodoRefId(id);
        return ResponseEntity.ok(itemList);
    }

    @GetMapping("/items/{id}")
    public Optional<Item> getItemById(@PathVariable("id") Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item;
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@RequestBody Item item, @PathVariable(name = "id") Long id) throws URISyntaxException {

        Item currentItem = itemRepository.getItemById(id);

        if (currentItem == null) {
            return new ResponseEntity("Unable to update. User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        currentItem = item;

        itemRepository.save(currentItem);
        return new ResponseEntity<>(currentItem, HttpStatus.OK);
    }

    @PostMapping("/items")
    public void addItem(@RequestBody Item item) {
        itemRepository.save(item);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) {
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
