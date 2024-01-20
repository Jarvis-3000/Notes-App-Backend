package com.notes.notes_app.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.notes.notes_app.entitiy.Note;

public interface NoteRepository extends MongoRepository<Note, ObjectId>{
  
}
