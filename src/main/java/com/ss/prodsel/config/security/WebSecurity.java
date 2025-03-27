package com.ss.prodsel.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth ->
        auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
          .permitAll()
          .anyRequest()
          .authenticated())
      .httpBasic(Customizer.withDefaults())
      .build();
  }

  @Bean
  UserDetailsService users() {

    UserDetails user1 = User.withDefaultPasswordEncoder()
      .username("user1")
      .password("user1")
      .roles("USER")
      .build();
    UserDetails admin = User.withDefaultPasswordEncoder()
      .username("admin1")
      .password("admin1")
      .roles("ADMIN")
      .build();
    return new InMemoryUserDetailsManager(user1, admin);
  }
}
