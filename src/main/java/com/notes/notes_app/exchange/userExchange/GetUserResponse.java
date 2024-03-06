package com.notes.notes_app.exchange.userExchange;

import com.notes.notes_app.model.UserEntity;

import lombok.Data;

@Data
public class GetUserResponse {
  private UserEntity user;
}
