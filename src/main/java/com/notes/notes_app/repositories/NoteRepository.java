package com.notes.notes_app.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.notes.notes_app.model.Note;

public interface NoteRepository extends MongoRepository<Note, String>{
  
}
