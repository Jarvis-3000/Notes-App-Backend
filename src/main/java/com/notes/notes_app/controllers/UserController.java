package com.notes.notes_app.controllers;

import java.util.List;

import org.bson.types.ObjectId;
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

import lombok.extern.log4j.Log4j2;

import com.notes.notes_app.apis.UserApi;
import com.notes.notes_app.model.User;
import com.notes.notes_app.services.UserServices;


@Log4j2
@RestController
@RequestMapping("/users")
public class UserController implements UserApi{
  @Autowired
  private UserServices userServices;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user){
    User createdUser = userServices.create(user);
    
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAll(){
    log.error("Request for All Users");
    List<User> users = userServices.getAll();
    log.warn("All users sent");
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable ObjectId id){
    User user = userServices.getById(id);

    if(user == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateById(@PathVariable ObjectId id, @RequestBody User user){
    User updatedUser = userServices.updateById(id, user);

    if(updatedUser == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @PutMapping("/username/{oldUsername}")
  public ResponseEntity<User> updateByUsername(@PathVariable String oldUsername, @RequestBody User user){
    User updatedUser = userServices.updateByUsername(oldUsername, user);

    if(updatedUser == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable ObjectId id){
    if(userServices.getById(id) ==  null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    userServices.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll(){
    userServices.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
