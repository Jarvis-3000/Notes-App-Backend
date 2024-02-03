package com.notes.notes_app.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.notes.notes_app.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId>{
    User getByUsername(String username);
}
