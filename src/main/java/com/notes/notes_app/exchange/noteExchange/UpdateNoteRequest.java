package com.notes.notes_app.exchange.noteExchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNoteRequest {
  @NotBlank
  private String title;
  @NotBlank
  private String description;
}

