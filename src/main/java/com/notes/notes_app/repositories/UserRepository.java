package com.notes.notes_app.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.notes.notes_app.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    boolean existsByUsername(String username);
    User getByUsername(String username);
}
