package com.notes.notes_app.exchange.userExchange;

import lombok.Data;

@Data
public class UpdateUserRequest {
  private String username;
  private String password;
}
