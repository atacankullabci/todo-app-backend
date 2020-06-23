package com.atacankullabci.todoapp.repository;


import com.atacankullabci.todoapp.common.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User getUserByUserName(String userName);
}
