package com.notes.notes_app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes.notes_app.model.Note;
import com.notes.notes_app.services.NoteServices;

// Using MockitoExtension to enable Mockito annotations
@ExtendWith(MockitoExtension.class)
public class NoteControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  // Mocking the NoteServices
  @Mock
  private NoteServices noteServices;

  // Injecting the mocks into the NoteController instance
  @InjectMocks
  private NoteController noteController;

  // Setting up MockMvc
  @BeforeEach
  void setup() {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
  }

  @Test
  @DisplayName("Test Note Creation with valid Data")
  public void testCreateNoteSuccess() throws Exception {
    // given
    Note inputNote = new Note("Title", "Description");
    Note mockedNote = new Note(new ObjectId(), "Title", "Description", null, null);

    // mocking the behaviour of creation of note by noteService
    when(noteServices.create(inputNote)).thenReturn(mockedNote);

    String requestBody = objectMapper.writeValueAsString(inputNote);

    // when
    // MockHttpServletRequestBuilder
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/notes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody));

    // then - verify the result or output using assert statements
    response
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @DisplayName("Test Note Creation with invalid Data")
  public void testCreateNoteBadRequest() throws Exception {
    // given
    Note inputNote = new Note(null, "Description");

    // NOTE: In this test we dont need to mock noteServices.create because when we
    // send wrong data to controller
    // Controller itself throws BAD_REQUEST error and doe not reach to this
    // noteService method
    // So, in this case since, this noteServices.create will not be used
    // Unnecessary Stubbing error will come
    // when(noteServices.create(inputNote)).thenThrow(NullPointerException.class);

    String requestBody = objectMapper.writeValueAsString(inputNote);

    // when
    // MockHttpServletRequestBuilder
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/notes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody));

    // then - verify the result or output using assert statements
    response
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  public void getAllNotes() throws Exception {
    // Given
    List<Note> mockNotes = Arrays.asList(
        new Note(new ObjectId(), "Title1", "Description1", null, null),
        new Note(new ObjectId(), "Title2", "Description2", null, null));

    // Mocking the behavior of the NoteServices getAll method
    when(noteServices.getAll()).thenReturn(mockNotes);

    // When
    // ResultActions resultActions =
    // mockMvc.perform(MockMvcRequestBuilders.get("/notes"));
    ResultActions response = mockMvc
        .perform(MockMvcRequestBuilders.get("/notes"));

    // Then
    response
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(mvcResponse -> {
          // Extract and print response content
          String responseContent = mvcResponse.getResponse().getContentAsString();
          List<Note> notes = objectMapper.readValue(responseContent, new TypeReference<List<Note>>() {
          });
          System.out.println("Response Content: " + notes);
        })
    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title1"))
    .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title2"));

    // Verifying that the NoteServices getAll method was called once
    verify(noteServices, times(1)).getAll();
  }

  @Test
  void getNoteById() throws Exception {
    // Given
    ObjectId id = new ObjectId();
    Note mockNote = new Note(id, "Test Title", "Test Description", null, null);

    // Mocking the behavior of the NoteServices getById method
    when(noteServices.getById(eq(id))).thenReturn(mockNote);

    // When
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/notes/{id}", id));

    // Then
    resultActions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Title"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"));

    // Verifying that the NoteServices getById method was called once with the
    // expected parameter
    verify(noteServices, times(1)).getById(eq(id));
  }

  @Test
  void updateNoteById() throws Exception {
    // Given
    ObjectId id = new ObjectId();
    Note inputNote = new Note("Updated Title", "Updated Description");
    Note mockedNote = new Note(id, "Updated Title", "Updated Description", null,
        null);

    // Mocking the behavior of the NoteServices updateById method
    when(noteServices.updateById(eq(id), any(Note.class))).thenReturn(mockedNote);

    String content = objectMapper.writeValueAsString(inputNote);

    // When
    ResultActions response = mockMvc
        .perform(MockMvcRequestBuilders
            .put("/notes/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(mvcResponse -> {
          // Extract and print response content
          String responseContent = mvcResponse.getResponse().getContentAsString();
          Note note = objectMapper.readValue(responseContent, Note.class);
          System.out.println(note);
        });

    // Verifying that the NoteServices updateById method was called once with the
    // expected parameters
    verify(noteServices, times(1)).updateById(eq(id), any(Note.class));
  }

  // @Test
  // void deleteNoteById() throws Exception {
  //   // Given
  //   ObjectId id = new ObjectId();

  //   // Mocking the behavior of the NoteServices getById and deleteById methods
  //   when(noteServices.deleteById(eq(id))).thenReturn(true);

  //   // When
  //   String uriBuilder = UriComponentsBuilder.fromPath("/notes/id/{id}").build(id).toUriString();
  //   String uri = uriBuilder.toUriString();

  //   ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(uri));

  //   // Then
  //   resultActions.andExpect(MockMvcResultMatchers.status().isOk());

  //   // Verifying that the NoteServices getById and deleteById methods were called
  //   // once with the expected parameter
  //   verify(noteServices, times(1)).getById(eq(id));
  //   verify(noteServices, times(1)).deleteById(eq(id));
  // }

  @Test
  void deleteAllNotes() throws Exception {
    // When
    when(noteServices.deleteAll()).thenReturn(true);

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/notes"));

    // Then
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    // Verifying that the NoteServices deleteAll method was called once
    verify(noteServices, times(1)).deleteAll();
  }

}