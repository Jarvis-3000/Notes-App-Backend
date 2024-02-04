package com.notes.notes_app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.exchange.userExchange.PostUserRequest;
import com.notes.notes_app.exchange.userExchange.UpdateUserRequest;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.model.User;
import com.notes.notes_app.repositories.UserRepository;

@Component
public class UserServices {
  @Autowired
  private UserRepository userRepository;

  public User create(PostUserRequest postUserRequest) {
    // prohibit duplicate username entry
    if (userRepository.existsByUsername(postUserRequest.getUsername())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    LocalDateTime dateTime = LocalDateTime.now();

    User user = new User(null, postUserRequest.getUsername(), postUserRequest.getPassword(), new ArrayList<>(),
        dateTime);

    User savedUser = userRepository.save(user);
    return savedUser;
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public List<User> findAll() {
    List<User> users = userRepository.findAll();

    return users;
  }

  public User findById(String id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given id"));
  }

  public boolean existsById(String id) {
    return userRepository.existsById(id);
  }

  public User updateById(String id, UpdateUserRequest updateUserRequest) throws ResponseStatusException {
    User oldUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given id"));

    if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().equals("")) {
      oldUser.setUsername(updateUserRequest.getUsername());
    }
    if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().equals("")) {
      oldUser.setPassword(updateUserRequest.getPassword());
    }

    User savedUser = userRepository.save(oldUser);

    return savedUser;
  }

  public User updateByUsername(String oldUsername, UpdateUserRequest updateUserRequest) throws ResponseStatusException {
    User oldUser = userRepository.getByUsername(oldUsername);

    if (oldUser == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given username");
    }

    if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().equals("")) {
      oldUser.setUsername(updateUserRequest.getUsername());
    }
    if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().equals("")) {
      oldUser.setPassword(updateUserRequest.getPassword());
    }

    User savedUser = userRepository.save(oldUser);

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

  public boolean noteExistByTitle(User user, String title){
    return user.getNotes().stream().anyMatch(existingNote -> existingNote.getTitle().toLowerCase().equals(title.toLowerCase()));
  }

  public User addNote(User user, Note newNote) {
    // add new note
    user.getNotes().add(newNote);

    User savedUser = userRepository.save(user);

    return savedUser;
  }

  public boolean deleteAllNotes(User user) {
    user.getNotes().clear();

    userRepository.save(user);

    return true;
  }
}
