package com.atacankullabci.todoapp;

import com.atacankullabci.todoapp.common.Item;
import com.atacankullabci.todoapp.common.Todo;
import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.common.enums.EnumItemDependency;
import com.atacankullabci.todoapp.common.enums.EnumItemStatus;
import com.atacankullabci.todoapp.repository.ItemRepository;
import com.atacankullabci.todoapp.repository.TodoRepository;
import com.atacankullabci.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.atacankullabci.todoapp"})
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(TodoRepository todoRepository) {
		return args -> {
			Item item1 = new Item(1L, 2L, "Test1", "", Date.from(Instant.now()), EnumItemStatus.UNDONE, EnumItemDependency.NOT_DEPENDENT);
			Item item2 = new Item(3L, 4L, "Test2", "", Date.from(Instant.now().plus(1, ChronoUnit.DAYS)), EnumItemStatus.UNDONE, EnumItemDependency.NOT_DEPENDENT);

			List<Item> itemList = new ArrayList<>();
			itemList.add(item1);
			itemList.add(item2);

			User user = new User("Atacan", "ata", "ata",
					"can", "test", new ArrayList<>());

			Todo todo = new Todo("Test Todo", itemList, user);
			todoRepository.save(todo);
			todoRepository.findAll().forEach(System.out::println);
		};
	}


}
