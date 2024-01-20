package com.notes.notes_app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notes.notes_app.entitiy.Note;
import com.notes.notes_app.repositories.NoteRepository;

@Component
public class NoteServices {
  @Autowired
  private NoteRepository noteRepository;

  public Note create(Note note){
    // we will get only title and description
    LocalDateTime dateTime = LocalDateTime.now();
    
    note.setCreatedAt(dateTime);
    note.setModifiedAt(dateTime);

    noteRepository.save(note); 
    return note;
  }

  public List<Note> getAll(){
    List<Note> notes = noteRepository.findAll();

    return notes;
  }

  public Note getById(ObjectId id){
    return noteRepository.findById(id).orElse(null);
  }


  public Note updateById(ObjectId id, Note note){
    Note oldNote = noteRepository.findById(id).orElse(null);

    if(oldNote == null){
      return null;
    }

    if(note.getTitle() != null && !note.getTitle().equals("")){
      oldNote.setTitle(note.getTitle());
    }
    if(note.getDescription() != null && !note.getDescription().equals("")){
      oldNote.setDescription(note.getDescription());
    }
    // update the modified date
    oldNote.setModifiedAt(LocalDateTime.now());

    return oldNote;
  }

  // public Note updateByTitle(Note note){
  //   // 
  //   return note;
  // }

  public boolean deleteById(ObjectId id){
    if(noteRepository.existsById(id)){
      noteRepository.deleteById(id);
      return true;
    }
    else{
      return false;
    }
  }

  public boolean deleteAll(){
    noteRepository.deleteAll();
    return true;
  }
}
