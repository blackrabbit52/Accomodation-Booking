package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Application;
import com.jaynie.accombooking.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<Application>>getApplicationByRoomId(@PathVariable String roomId) {
        return new ResponseEntity<>(service.getApplicationsByRoomId(roomId), HttpStatus.OK);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(service.getApplicationsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/applicationId/{applicationId}")
    public ResponseEntity<List<Application>> getApplicationsByApplicationId(@PathVariable String applicationId) {
        return new ResponseEntity<>(service.getApplicationById(applicationId), HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> createApplication(@RequestBody Application.ApplicationRequest applicationRequest) {
        Application createdApplication = service.createApplication(applicationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    @GetMapping("/user/{userId}/applications")
    public ResponseEntity<List<Application>> getUserApplications(@PathVariable String userId) {
        return new ResponseEntity<>(service.getApplicationsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/status/{applicationId}")
    public HttpEntity<List<Application>> getApplicationStatus(@PathVariable String status) {
        return new ResponseEntity<>(service.getApplicationStatus(status),HttpStatus.OK);
    }
}
