package com.atacankullabci.todoapp.repository;


import com.atacankullabci.todoapp.common.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserName(String userName);

}
