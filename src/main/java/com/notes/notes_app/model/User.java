package com.notes.notes_app.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Document
@Data
@AllArgsConstructor
public class User {
  @Id
  private ObjectId id;

  @NotBlank
  @Indexed(unique = true)
  private String username;

  @NotBlank
  private String password;

  private List<Note> notes;

  private LocalDateTime createdAt;
}
