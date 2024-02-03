package com.notes.notes_app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notes.notes_app.model.User;
import com.notes.notes_app.repositories.UserRepository;

@Component
public class UserServices {
  @Autowired
  private UserRepository userRepository;

  public User create(User user){
    // we will get only username and password
    LocalDateTime dateTime = LocalDateTime.now();
    
    user.setCreatedAt(dateTime);
    user.setNotes(new ArrayList<>());


    userRepository.save(user); 
    return user;
  }

  public List<User> getAll(){
    List<User> users = userRepository.findAll();

    return users;
  }

  public User getById(ObjectId id){
    return userRepository.findById(id).orElse(null);
  }


  public User updateById(ObjectId id, User user){
    User oldUser = userRepository.findById(id).orElse(null);

    if(oldUser == null){
      return null;
    }

    if(user.getUsername() != null && !user.getUsername().equals("")){
      oldUser.setUsername(user.getUsername());
    }
    if(user.getPassword() != null && !user.getPassword().equals("")){
      oldUser.setPassword(user.getPassword());
    }
    

    return oldUser;
  }

  public User updateByUsername(String oldUsername, User user){
    User oldUser = userRepository.getByUsername(oldUsername);

    if(oldUser == null){
      return null;
    }

    oldUser.setUsername(user.getUsername());
    oldUser.setPassword(user.getPassword());

    return oldUser;
  }

  public boolean deleteById(ObjectId id){
    if(userRepository.existsById(id)){
      userRepository.deleteById(id);
      return true;
    }
    else{
      return false;
    }
  }

  public boolean deleteAll(){
    userRepository.deleteAll();
    return true;
  }
}
