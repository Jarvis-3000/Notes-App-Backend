package com.notes.notes_app.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Note {
  @Id
  private ObjectId id;

  @Indexed(unique = true)
  @NonNull
  private String title;

  @NonNull
  private String description;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public Note(String title, String description) {
    this.title = title;
    this.description = description;
  }
}
