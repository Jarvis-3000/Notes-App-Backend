package com.notes.notes_app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.enums.Role;
import com.notes.notes_app.exchange.userExchange.PostUserRequest;
import com.notes.notes_app.exchange.userExchange.UpdateUserRequest;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.model.UserEntity;
import com.notes.notes_app.repositories.UserRepository;

@Component
public class UserServices {
  @Autowired
  private UserRepository userRepository;
  
  public UserEntity save(UserEntity user) {
    return userRepository.save(user);
  }

  public List<UserEntity> findAll() {
    List<UserEntity> users = userRepository.findAll();

    return users;
  }

  public UserEntity findById(String id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given id"));
  }

  public boolean existsById(String id) {
    return userRepository.existsById(id);
  }

  public UserEntity updateById(String id, UpdateUserRequest updateUserRequest) throws ResponseStatusException {
    UserEntity oldUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given id"));

    if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().equals("")) {
      oldUser.setUsername(updateUserRequest.getUsername());
    }
    if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().equals("")) {
      oldUser.setPassword(updateUserRequest.getPassword());
    }

    UserEntity savedUser = userRepository.save(oldUser);

    return savedUser;
  }

  public UserEntity updateByUsername(String oldUsername, UpdateUserRequest updateUserRequest)
      throws ResponseStatusException {
    UserEntity oldUser = userRepository.findByUsername(oldUsername)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given username"));

    if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().equals("")) {
      oldUser.setUsername(updateUserRequest.getUsername());
    }
    if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().equals("")) {
      oldUser.setPassword(updateUserRequest.getPassword());
    }

    UserEntity savedUser = userRepository.save(oldUser);

    return savedUser;
  }

  public boolean deleteById(String id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  public boolean deleteAll() {
    userRepository.deleteAll();
    return true;
  }

  public boolean noteExistByTitle(UserEntity user, String title) {
    return user.getNotes().stream()
        .anyMatch(existingNote -> existingNote.getTitle().toLowerCase().equals(title.toLowerCase()));
  }

  public UserEntity addNote(UserEntity user, Note newNote) {
    // add new note
    user.getNotes().add(newNote);

    UserEntity savedUser = userRepository.save(user);

    return savedUser;
  }

  public boolean deleteAllNotes(UserEntity user) {
    user.getNotes().clear();

    userRepository.save(user);

    return true;
  }
}
