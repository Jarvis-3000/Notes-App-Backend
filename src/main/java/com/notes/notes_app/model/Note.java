package com.notes.notes_app.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "notes")
@NoArgsConstructor
@AllArgsConstructor
public class Note {
  @Id
  private String id;

  @Indexed(unique = true)
  @NotBlank(message = "Title is mandatory")
  private String title;

  @NotBlank(message = "Description is mandatory")
  private String description;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
