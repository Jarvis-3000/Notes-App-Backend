package com.notes.notes_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.notes.notes_app.model.Note;
import com.notes.notes_app.repositories.NoteRepository;

// Using MockitoExtension to enable Mockito annotations
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

  // Mocking the NoteRepository
  @Mock
  private NoteRepository noteRepository;

  // Injecting the mocks into the NoteServices instance
  @InjectMocks
  private NoteServices noteServices;

  @BeforeEach
  void setup() {
    // Reset mock before each test
    reset(noteRepository);
  }

  @Test
  @DisplayName("Testing Note Creation/Save in Database")
  void testNoteCreationSuccess() {
    // Given
    Note inputNote = new Note();
    inputNote.setTitle("Test Title");
    inputNote.setDescription("Test Description");

    LocalDateTime fixedDateTime = LocalDateTime.now();

    // Mocking the behaviour of the repository saved method
    when(noteRepository.save(any(Note.class)))
        .thenReturn(new Note(new ObjectId(), "Test Title", "Test Description", fixedDateTime, fixedDateTime));

    // when
    Note savedNote = noteServices.create(inputNote);

    // then

    // Verifying that the repository's save method was called once with the expected
    // parameters
    verify(noteRepository, times(1)).save(inputNote);

    assertNotNull(savedNote);
    assertNotNull(savedNote.getId());
    assertEquals("Test Title", savedNote.getTitle());
    assertEquals("Test Description", savedNote.getDescription());
    assertNotNull(savedNote.getCreatedAt());
    assertNotNull(savedNote.getModifiedAt());
  }

  @Test
  @DisplayName("Test Get All Notes Method")
  void testGetAllNotesSuccess() {
    // given
    Note inputNote1 = new Note("Title 1", "Description 1");
    Note inputNote2 = new Note("Title 2", "Description 2");
    Note inputNote3 = new Note("Title 3", "Description 3");

    List<Note> inputNotes = new ArrayList<>();

    inputNotes.add(inputNote1);
    inputNotes.add(inputNote2);
    inputNotes.add(inputNote3);

    // mocking the behaviour of findAll method of repository
    when(noteRepository.findAll()).thenReturn(inputNotes);

    // when
    List<Note> result = noteServices.getAll();

    // then
    assertNotNull(result);
    assertEquals(3, result.size());

    // Verifying that the repository's findAll method was called once
    verify(noteRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Test Get Note By Id")
  void testGetNoteByIdSuccess() {
    // given
    ObjectId id = new ObjectId();
    LocalDateTime fixedDateTime = LocalDateTime.now();
    Note mockNote = new Note(id, "Title", "Description", fixedDateTime, fixedDateTime);

    // mocking the behaviour of findById method of repository
    when(noteRepository.findById(eq(id))).thenReturn(Optional.of(mockNote));

    // when
    Note result = noteServices.getById(id);

    // then
    assertNotNull(result);
    assertEquals("Title", result.getTitle());
    assertEquals("Description", result.getDescription());

    // Verifying that the repository's findById method was called once with the
    // expected parameter
    verify(noteRepository, times(1)).findById(eq(id));
  }

  @Test
  @DisplayName("Test Get Note By Id -> Null Note")
  void testGetNoteByIdThrowException() {
    // mocking the behaviour of findById method of repository id not found
    when(noteRepository.findById(any(ObjectId.class))).thenReturn(Optional.ofNullable(null));

    // when
    Note result = noteServices.getById(new ObjectId());

    // then
    assertNull(result);

    // Verifying that the repository's findById method was called once with the
    // expected parameter
    verify(noteRepository, times(1)).findById(any(ObjectId.class));
  }

  @Test
  @DisplayName("Test Update Note By Id")
  void updateNoteById() {
    // Given
    ObjectId id = new ObjectId();
    Note existingNote = new Note(id, "Existing Title", "Existing Description", null, null);
    Note updatedNote = new Note(null, "Updated Title", "Updated Description", null, null);

    // Mocking the behavior of the repository findById and save methods
    when(noteRepository.findById(eq(id))).thenReturn(Optional.of(existingNote));
    when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

    // When
    Note result = noteServices.updateById(id, updatedNote);

    // Then
    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
    assertEquals("Updated Description", result.getDescription());

    // Verifying that the repository's findById and save methods were called once
    // with the expected parameters
    verify(noteRepository, times(1)).findById(eq(id));
    verify(noteRepository, times(1)).save(any(Note.class));
  }

  @Test
  @DisplayName("Test Delete Note By Id")
  void deleteNoteById() {
    // Given
    ObjectId id = new ObjectId();

    // Mocking the behavior of the repository existsById and deleteById methods
    when(noteRepository.existsById(eq(id))).thenReturn(true);

    // When
    boolean result = noteServices.deleteById(id);

    // Then
    assertTrue(result);

    // Verifying that the repository's existsById and deleteById methods were called
    // once with the expected parameter
    verify(noteRepository, times(1)).existsById(eq(id));
    verify(noteRepository, times(1)).deleteById(eq(id));
  }

  @Test
  @DisplayName("Test Delete All Notes")
  void deleteAllNotes() {
    // When
    boolean result = noteServices.deleteAll();

    // Then
    assertTrue(result);

    // Verifying that the repository's deleteAll method was called once
    verify(noteRepository, times(1)).deleteAll();
  }

}
