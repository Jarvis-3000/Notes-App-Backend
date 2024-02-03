package com.notes.notes_app.apis;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.notes.notes_app.model.Note;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Notes", description = "Notes Endpoints")
public interface NoteApi {

  @Operation(summary = "Create a new note", description = "Creates a new note and returns the created note")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "successfully created a note", content = {
          @Content(schema = @Schema(implementation = Note.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "409", description = "duplicate note title")
  })
  public ResponseEntity<Note> create(@RequestBody Note note);

  @Operation(summary = "Get all notes", description = "Get all notes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all the notes")
  })
  public ResponseEntity<List<Note>> getAll();

  @Operation(summary = "Get the note by id", description = "Get the note by id if exist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the note by id"),
      @ApiResponse(responseCode = "404", description = "Operation failed because id does not exist"),
  })
  public ResponseEntity<Note> getById(@PathVariable ObjectId id);

  @Operation(summary = "Get the notes by title substring matching", description = "Get all notes with matching title subsctring. If no matching title found then empty list will be returned")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all the notes by title substring")
  })
  public ResponseEntity<List<Note>> getByTitle(@RequestParam String title);

  @Operation(summary = "Get the notes by created date", description = "Get all notes by created date. If no note found created in the date range then empty list will be returned")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all the notes by created date")
  })
  public ResponseEntity<List<Note>> getByCreatedDateRange(@RequestParam String start, @RequestParam String end);

  @Operation(summary = "Update the note by id", description = "Update the note by id and return updated note")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated the note"),
      @ApiResponse(responseCode = "404", description = "Id not found"),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Note> updateById(@PathVariable ObjectId id, @RequestBody Note note);

  @Operation(summary = "Delete the note by id", description = "Delete the note by id and return status of deletion")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully deleted the note"),
      @ApiResponse(responseCode = "404", description = "Id not found")
  })
  public ResponseEntity<?> deleteById(@PathVariable ObjectId id);

  @Operation(summary = "Delete all the notes", description = "Delete all the notes and return status of deletion")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully deleted all the notes"),
  })
  public ResponseEntity<?> deleteAll();

}
