package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.User;
import com.jaynie.accombooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(service.findAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable String id) {
        return new ResponseEntity<>(service.findUserById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public User createUser(@RequestBody User user) {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.OK).getBody();
    }









}





