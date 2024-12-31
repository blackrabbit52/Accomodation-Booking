package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Application;
import com.jaynie.accombooking.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    @Autowired
    private final ApplicationService service;
    @Autowired
    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Application>> getAllApplications() {
        return new ResponseEntity<>(service.getAllApplications(), HttpStatus.OK);
    }


}
