package com.notes.notes_app.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.notes.notes_app.model.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
