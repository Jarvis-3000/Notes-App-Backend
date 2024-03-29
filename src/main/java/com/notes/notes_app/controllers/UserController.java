package com.notes.notes_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.log4j.Log4j2;

import com.notes.notes_app.apis.UserApi;
import com.notes.notes_app.exchange.userExchange.PostUserRequest;
import com.notes.notes_app.exchange.userExchange.UpdateUserRequest;
import com.notes.notes_app.model.UserEntity;
import com.notes.notes_app.services.UserServices;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController implements UserApi {
  @Autowired
  private UserServices userServices;


  @GetMapping
  public ResponseEntity<List<UserEntity>> getAll() {
    log.error("Request for All Users");
    List<UserEntity> users = userServices.findAll();
    log.warn("All users sent");
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserEntity> getById(@PathVariable String id) {
    try {
      UserEntity user = userServices.findById(id);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (ResponseStatusException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserEntity> updateById(@PathVariable String id, @RequestBody UpdateUserRequest updateUserRequest) {
    try {
      UserEntity updatedUser = userServices.updateById(id, updateUserRequest);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    } catch (ResponseStatusException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/username/{oldUsername}")
  public ResponseEntity<UserEntity> updateByUsername(@PathVariable String oldUsername,
      @RequestBody UpdateUserRequest updateUserRequest) {
    try {
      UserEntity updatedUser = userServices.updateByUsername(oldUsername, updateUserRequest);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    } catch (ResponseStatusException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {

    if (userServices.existsById(id)) {
      userServices.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll() {
    userServices.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
