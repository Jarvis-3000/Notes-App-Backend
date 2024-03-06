package com.notes.notes_app.exchange.authExachange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessfullResponse {
  private String message;
  private String token;
}
