package com.notes.notes_app.controller;

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

import com.notes.notes_app.entitiy.User;
import com.notes.notes_app.services.UserServices;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserServices userServices;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user){
    User createdUser = userServices.create(user);
    
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAll(){
    List<User> users = userServices.getAll();
    
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<User> getById(@PathVariable ObjectId id){
    User user = userServices.getById(id);

    if(user == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PutMapping("/id/{id}")
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

  @DeleteMapping("/id/{id}")
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
