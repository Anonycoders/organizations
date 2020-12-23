package com.hospitad.organization.controllers;


import com.hospitad.organization.models.User;
import com.hospitad.organization.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllersTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String username = "user";
    private final String wrong_username = "user_name";
    private final String email = "email@gmail.com";
    private final String password = "password";


    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    public void postToken_SucceedToLogin() {
        registerUser();

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/tokens",
                Map.of("username", username, "password", password), Object.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void postToken_failToLogin() {
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/tokens",
                Map.of("username", wrong_username, "password", password), Object.class);

        Assertions.assertEquals(401, responseEntity.getStatusCodeValue());
    }

    @Test
    public void postUser_failToRegisterWithNullValue() {
        User user = new User(username, null, password);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/users/register",
                requestEntity, Object.class);

        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void postUser_succeedToRegister() {
        User user = new User(username, email, password);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("/users/register", requestEntity, User.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }

    private void registerUser(){
        User user = new User(username, email, passwordEncoder.encode(password));
        userRepository.save(user);
    }


    private String generateToken() {
        final Map<String, String> tokenMap = restTemplate.postForObject("/tokens", Map.of("username", username, "password", password), Map.class);
        return tokenMap.get("token");
    }
}
