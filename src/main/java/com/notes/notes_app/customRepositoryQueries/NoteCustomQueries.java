package com.notes.notes_app.customRepositoryQueries;

import java.time.LocalDate;
import java.util.List;

import com.notes.notes_app.model.Note;

public interface NoteCustomQueries {
  boolean existByTitleContainingIgnoreCase(String title) throws IllegalArgumentException;

  List<Note> findByTitleContainingIgnoreCase(String titleSubString);

  List<Note> findByDescriptionContainingIgnoreCase(String descriptionSubString);

  List<Note> findByCreatedDateRange(LocalDate startDate, LocalDate endDate);
}
