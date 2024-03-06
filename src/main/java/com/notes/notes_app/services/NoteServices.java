package com.notes.notes_app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.notes.notes_app.exchange.noteExchange.PostNoteRequest;
import com.notes.notes_app.exchange.noteExchange.UpdateNoteRequest;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.model.UserEntity;
import com.notes.notes_app.repositories.NoteRepository;

@Component
public class NoteServices {
  @Autowired
  private UserServices userServices;

  @Autowired
  private NoteRepository noteRepository;

  public Note addNoteToUser(String userId, PostNoteRequest postNoteRequest) throws ResponseStatusException {
    UserEntity user = getUserOrThrowException(userId);

    LocalDateTime dateTime = LocalDateTime.now();

    Note note = new Note(null, postNoteRequest.getTitle(), postNoteRequest.getDescription(), dateTime, dateTime);

    Note savedNote = noteRepository.save(note);

    if (userServices.noteExistByTitle(user, postNoteRequest.getTitle())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    // user will get saved
    userServices.addNote(user, savedNote);
    return savedNote;
  }

  public List<Note> getAllNotesOfUser(String userId) {
    UserEntity user = getUserOrThrowException(userId);

    return user.getNotes();
  }

  public Note getById(String id) {
    return noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public List<Note> getByTitle(String userId, String titleSubString) {
    UserEntity user = getUserOrThrowException(userId);

    List<Note> filteredNotes = user.getNotes().stream()
        .filter(note -> {
          boolean isMatching = note.getTitle().toLowerCase().contains(titleSubString.toLowerCase());
          return isMatching;
        })
        .collect(Collectors.toList());

    return filteredNotes;
  }

  public List<Note> getByCreatedDateRange(String userId, LocalDate startDate, LocalDate endDate) {
    UserEntity user = getUserOrThrowException(userId);

    List<Note> notes = user.getNotes();

    List<Note> filteredNotes = notes.stream()
        .filter(note -> {
          return isDateBetween(note.getCreatedAt(), startDate, endDate);
        })
        .collect(Collectors.toList());

    return filteredNotes;
  }

  public Note updateById(String noteId, UpdateNoteRequest updateNoteRequest) {
    Note oldNote = noteRepository.findById(noteId).orElse(null);

    if (oldNote == null) {
      return null;
    }

    if (updateNoteRequest.getTitle() != null && !updateNoteRequest.getTitle().equals("")) {
      oldNote.setTitle(updateNoteRequest.getTitle());
    }
    if (updateNoteRequest.getDescription() != null && !updateNoteRequest.getDescription().equals("")) {
      oldNote.setDescription(updateNoteRequest.getDescription());
    }
    // update the modified date
    oldNote.setModifiedAt(LocalDateTime.now());

    // save the updated note in repository
    Note savedNote = noteRepository.save(oldNote);

    return savedNote;
  }

  public boolean deleteById(String userId, String noteId) throws ResponseStatusException {
    UserEntity user = userServices.findById(userId);

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given id");
    }
    if (!noteRepository.existsById(noteId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found for given id");
    }

    List<Note> filteredNotes = user.getNotes().stream()
        .filter(note -> !note.getId().toString().equals(noteId))
        .collect(Collectors.toList());

    user.setNotes(filteredNotes);
    userServices.save(user);

    noteRepository.deleteById(noteId);
    return true;
  }

  public boolean deleteAll(String userId) {
    UserEntity user = getUserOrThrowException(userId);

    // Delete notes from the NoteRepository
    for (Note note : user.getNotes()) {
      noteRepository.deleteById(note.getId());
    }

    return userServices.deleteAllNotes(user);
  }

  private boolean isDateBetween(LocalDateTime dateTime, LocalDate startDate, LocalDate endDate) {
    LocalDate date = dateTime.toLocalDate();
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  private UserEntity getUserOrThrowException(String id) throws ResponseStatusException {
    try {
      UserEntity user = userServices.findById(id);
      return user;
    } catch (Exception e) {
      throw e;
    }
  }
}
