package com.notes.notes_app.controllers;

import java.util.Collections;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notes.notes_app.config.JwtUtil;
import com.notes.notes_app.enums.Role;
// import com.notes.notes_app.config.JwtUtil;
import com.notes.notes_app.exchange.authExachange.LoginRequest;
import com.notes.notes_app.exchange.authExachange.LoginSuccessfullResponse;
import com.notes.notes_app.exchange.authExachange.RegisterRequest;
import com.notes.notes_app.exchange.authExachange.RegisterationSuccessfullResponse;
import com.notes.notes_app.model.UserEntity;
import com.notes.notes_app.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    // prohibit duplicate username entry
    if (userRepository.existsByUsername(registerRequest.getUsername())) {
      return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
    }

    UserEntity userEntity = new UserEntity();
    LocalDateTime dateTime = LocalDateTime.now();

    userEntity.setUsername(registerRequest.getUsername());
    userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    userEntity.setRoles(Collections.singleton(Role.USER));
    userEntity.setNotes(new ArrayList<>());
    userEntity.setCreatedAt(dateTime);

    userRepository.save(userEntity);

    String message = "User registration successfull";
    return new ResponseEntity<>(new RegisterationSuccessfullResponse(message), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginSuccessfullResponse> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String message = "User login successfull";
    String token = jwtUtil.generateToken(authentication);

    return new ResponseEntity<>(new LoginSuccessfullResponse(message, token), HttpStatus.CREATED);
  }
}
