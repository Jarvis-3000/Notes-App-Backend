package com.notes.notes_app.exchange.userExchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostUserRequest {
  @NotBlank
  private String username;
  @NotBlank
  private String password;
}
