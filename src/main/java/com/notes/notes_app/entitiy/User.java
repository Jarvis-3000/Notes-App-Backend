package com.notes.notes_app.entitiy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Document
@Data
@AllArgsConstructor
public class User {
  @Id
  private ObjectId id;

  @NonNull
  @Indexed(unique = true)
  private String username;
  
  @NonNull
  private String password;

  private List<Note> notes;

  private LocalDateTime createdAt;
}
