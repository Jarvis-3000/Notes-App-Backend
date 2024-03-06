package com.notes.notes_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  // @Autowired
  // private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private JwtAuthEntryPoint jwtAuthEntryPoint;

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  // @Bean
  // public DaoAuthenticationProvider authenticationProvider() {
  //   DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

  //   authProvider.setUserDetailsService(userDetailsServiceImpl);
  //   authProvider.setPasswordEncoder(passwordEncoder());

  //   return authProvider;
  // }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf((csrf) -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((authorize) -> {
          authorize
              .requestMatchers("/auth/**").permitAll()
              .anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());

    // http.authenticationProvider(authenticationProvider());

    // Add JWT token filter
    http.addFilterBefore(
        jwtAuthFilter,
        UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
