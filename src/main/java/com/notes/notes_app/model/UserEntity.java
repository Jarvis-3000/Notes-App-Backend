package com.notes.notes_app.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.notes.notes_app.enums.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  private String id;

  @NotBlank
  @Indexed(unique = true)
  private String username;

  @NotBlank
  private String password;

  private Set<Role> roles;

  @DBRef
  private List<Note> notes;

  private LocalDateTime createdAt;
}
