package com.notes.notes_app.exchange.userExchange;

import java.util.List;

import com.notes.notes_app.model.User;

import lombok.Data;

@Data
public class GetAllUsersResponse {
  private List<User> users;
}
