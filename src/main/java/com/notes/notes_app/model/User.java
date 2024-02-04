package com.notes.notes_app.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "users")
@Data
@AllArgsConstructor
public class User {
  @Id
  private String id;

  @NotBlank
  @Indexed(unique = true)
  private String username;

  @NotBlank
  private String password;

  @DBRef
  private List<Note> notes = new ArrayList<>();

  private LocalDateTime createdAt;
}
