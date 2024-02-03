package com.notes.notes_app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.customRepositoryQueries.NoteCustomQueries;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.repositories.NoteRepository;

@Component
public class NoteServices {
  @Autowired
  private NoteRepository noteRepository;

  @Autowired
  NoteCustomQueries noteCustomQueries;

  public Note create(Note inputNote) throws ResponseStatusException{
    if(noteCustomQueries.existByTitleContainingIgnoreCase(inputNote.getTitle())){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Note with same title already exist!");
    }

    // we will get only title and description
    LocalDateTime dateTime = LocalDateTime.now();
    
    inputNote.setCreatedAt(dateTime);
    inputNote.setModifiedAt(dateTime);

    Note savedNote = noteRepository.save(inputNote); 
    return savedNote;
  }

  public List<Note> getAll() {
    List<Note> notes = noteRepository.findAll();

    return notes;
  }

  public Note getById(ObjectId id) {
    return noteRepository.findById(id).orElse(null);
  }

  public List<Note> getByTitle(String titleSubString) {
    return noteCustomQueries.findByTitleContainingIgnoreCase(titleSubString);
  }

  public List<Note> getByCreatedDateRange(LocalDate startDate, LocalDate endDate) {
    return noteCustomQueries.findByCreatedDateRange(startDate, endDate);
  }

  public Note updateById(ObjectId id, Note note) {
    Note oldNote = noteRepository.findById(id).orElse(null);

    if (oldNote == null) {
      return null;
    }

    if (note.getTitle() != null && !note.getTitle().equals("")) {
      oldNote.setTitle(note.getTitle());
    }
    if (note.getDescription() != null && !note.getDescription().equals("")) {
      oldNote.setDescription(note.getDescription());
    }
    // update the modified date
    oldNote.setModifiedAt(LocalDateTime.now());

    // save the updated note in repository
    Note savedNote = noteRepository.save(oldNote);

    return savedNote;
  }

  public boolean deleteById(ObjectId id) {
    if (noteRepository.existsById(id)) {
      noteRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  public boolean deleteAll() {
    noteRepository.deleteAll();
    return true;
  }
}
