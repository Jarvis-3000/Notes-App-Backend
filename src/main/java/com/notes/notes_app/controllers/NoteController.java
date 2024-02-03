package com.notes.notes_app.controllers;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.apis.NoteApi;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.services.NoteServices;

@RestController
@RequestMapping("/notes")
public class NoteController implements NoteApi {
  @Autowired
  private NoteServices noteServices;

  @PostMapping
  public ResponseEntity<Note> create(@RequestBody Note note) {
    try {
      Note createdNote = noteServices.create(note);
      return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @GetMapping
  public ResponseEntity<List<Note>> getAll() {
    List<Note> notes = noteServices.getAll();

    return new ResponseEntity<>(notes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Note> getById(@PathVariable ObjectId id) {
    Note note = noteServices.getById(id);

    if (note == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(note, HttpStatus.OK);
  }

  @GetMapping("/title")
  public ResponseEntity<List<Note>> getByTitle(@RequestParam String title) {
    List<Note> note = noteServices.getByTitle(title);

    if (note == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(note, HttpStatus.OK);
  }

  @GetMapping("/dates")
  public ResponseEntity<List<Note>> getByCreatedDateRange(@RequestParam String start, @RequestParam String end) {
    List<Note> note = noteServices.getByCreatedDateRange(LocalDate.parse(start), LocalDate.parse(end));

    if (note == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(note, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Note> updateById(@PathVariable ObjectId id, @RequestBody Note note) {
    Note updatedNote = noteServices.updateById(id, note);

    if (updatedNote == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(updatedNote, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable ObjectId id) {
    if (noteServices.getById(id) == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    noteServices.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll() {
    noteServices.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
