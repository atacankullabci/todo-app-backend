package com.atacankullabci.todoapp.repository;

import com.atacankullabci.todoapp.common.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
}
