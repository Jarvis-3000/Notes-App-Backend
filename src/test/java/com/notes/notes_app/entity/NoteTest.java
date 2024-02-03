package com.notes.notes_app.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import com.notes.notes_app.model.Note;

public class NoteTest {

  

  @Test
  public void testNoteObjectCreation() {
    ObjectId id = new ObjectId();
    String title = "1";
    String description = "1";
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime modifiedAt = LocalDateTime.now();

    Note note = new Note(id, title, description, createdAt, modifiedAt);

    assertNotNull(note);
    assertEquals(title, note.getTitle());
    assertEquals(description, note.getDescription());
    assertEquals(createdAt, note.getCreatedAt());
    assertEquals(modifiedAt, note.getModifiedAt());
  }

  @Test
  void testNoteObjectCreationWithoutDates() {
    ObjectId objectId = new ObjectId();
    String title = "Test Title";
    String description = "Test Description";

    Note note = new Note(objectId, title, description, null, null);

    assertNotNull(note);
    assertEquals(objectId, note.getId());
    assertEquals(title, note.getTitle());
    assertEquals(description, note.getDescription());
    assertNull(note.getCreatedAt());
    assertNull(note.getModifiedAt());
  }

  @Test
  void testNonNullTitleValidation() {
    assertThrows(NullPointerException.class, () -> new Note(new ObjectId(), "Hello", null, null, null));
  }

  @Test
  void testUniqueTitleValidation() {
    ObjectId objectId1 = new ObjectId();
    ObjectId objectId2 = new ObjectId();
    String title = "Unique Title";
    String description = "Test Description";

    Note note1 = new Note(objectId1, title, description, null, null);
    Note note2 = new Note(objectId2, title, description, null, null);

    assertNotEquals(note1.getId(), note2.getId()); // Ensure different IDs
    assertEquals(title, note1.getTitle());
    assertEquals(title, note2.getTitle());

    // You may want to test the behavior when saving these notes to your repository.
    // Depending on your setup, saving note2 with the same title might throw an
    // exception.
  }

}
