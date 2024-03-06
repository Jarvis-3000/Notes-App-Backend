package com.notes.notes_app.exchange.userExchange;

import java.util.List;

import com.notes.notes_app.model.UserEntity;

import lombok.Data;

@Data
public class GetAllUsersResponse {
  private List<UserEntity> users;
}
