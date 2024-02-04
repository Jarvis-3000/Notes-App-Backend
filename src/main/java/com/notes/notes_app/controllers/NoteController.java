package com.notes.notes_app.controllers;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.apis.NoteApi;
import com.notes.notes_app.exchange.noteExchange.PostNoteRequest;
import com.notes.notes_app.exchange.noteExchange.UpdateNoteRequest;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.services.NoteServices;

@RestController
@RequestMapping("/users/{userId}/notes")
public class NoteController implements NoteApi {
  @Autowired
  private NoteServices noteServices;

  @PostMapping
  public ResponseEntity<Note> addNoteToUser(@PathVariable String userId, @RequestBody PostNoteRequest postNoteRequest) {
    try {
      Note createdNote = noteServices.addNoteToUser(userId, postNoteRequest);
      return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }

  @GetMapping
  public ResponseEntity<List<Note>> getAllNotesOfUser(@PathVariable String userId) {
    try {
      List<Note> notes = noteServices.getAllNotesOfUser(userId);
      return new ResponseEntity<>(notes, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Note> getById(@PathVariable String id) {
    try {
      Note note = noteServices.getById(id);
      return new ResponseEntity<>(note, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }

  @GetMapping("/title")
  public ResponseEntity<List<Note>> getByTitle(@PathVariable String userId, @RequestParam String title) {
    try {
      List<Note> note = noteServices.getByTitle(userId, title);
      return new ResponseEntity<>(note, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }

  @GetMapping("/dates")
  public ResponseEntity<List<Note>> getByCreatedDateRange(@PathVariable String userId, @RequestParam String start,
      @RequestParam String end) {
    try {
      LocalDate startDate = LocalDate.parse(start);
      LocalDate endDate = LocalDate.parse(end);

      List<Note> note = noteServices.getByCreatedDateRange(userId, startDate, endDate);
      return new ResponseEntity<>(note, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{noteId}")
  public ResponseEntity<Note> updateById(@PathVariable String userId, @PathVariable String noteId,
      @RequestBody UpdateNoteRequest updateNoteRequest) {
    Note updatedNote = noteServices.updateById(noteId, updateNoteRequest);

    if (updatedNote == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(updatedNote, HttpStatus.OK);
  }

  @DeleteMapping("/{noteId}")
  public ResponseEntity<Boolean> deleteById(@PathVariable String userId, @PathVariable String noteId) {
    try {
      boolean response = noteServices.deleteById(userId, noteId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll(@PathVariable String userId) {
    try {
      boolean response = noteServices.deleteAll(userId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException exception) {
      return new ResponseEntity<>(exception.getStatusCode());
    }
  }
}
