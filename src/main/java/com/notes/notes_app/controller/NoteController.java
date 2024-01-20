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

import com.notes.notes_app.entitiy.Note;
import com.notes.notes_app.services.NoteServices;

@RestController
@RequestMapping("/notes")
public class NoteController {
  @Autowired
  private NoteServices noteServices;

  @PostMapping
  public ResponseEntity<Note> create(@RequestBody Note note){
    Note createdNote = noteServices.create(note);
    
    return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Note>> getAll(){
    List<Note> notes = noteServices.getAll();
    
    return new ResponseEntity<>(notes, HttpStatus.OK);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<Note> getById(@PathVariable ObjectId id){
    Note note = noteServices.getById(id);

    if(note == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(note, HttpStatus.OK);
  }

  @PutMapping("/id/{id}")
  public ResponseEntity<Note> updateById(@PathVariable ObjectId id, @RequestBody Note note){
    Note updatedNote = noteServices.updateById(id, note);

    if(updatedNote == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(updatedNote, HttpStatus.OK);
  }
  

  @DeleteMapping("/id/{id}")
  public ResponseEntity<?> deleteById(@PathVariable ObjectId id){
    if(noteServices.getById(id) ==  null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    noteServices.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll(){
    noteServices.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
