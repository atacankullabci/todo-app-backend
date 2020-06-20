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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.atacankullabci.todoapp"})
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}

	/*@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {

			User user = new User("Atacan", "ata", "ata",
					"can", "test", new ArrayList<>());

			userRepository
					.save(user);
		};
	}*/
}
