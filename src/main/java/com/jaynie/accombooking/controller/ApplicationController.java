package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Application;
import com.jaynie.accombooking.rabbitMq.NotificationPublisher;
import com.jaynie.accombooking.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/application")
@CrossOrigin(origins = "http://localhost:63342") // allowing frontend access

public class ApplicationController {
    @Autowired
    private final ApplicationService service;

    private final NotificationPublisher notificationPublisher;


    @Autowired
    public ApplicationController(ApplicationService service, NotificationPublisher notificationPublisher) {
        this.service = service;
        this.notificationPublisher = notificationPublisher;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Application>> getAllApplications() {
        return new ResponseEntity<>(service.getAllApplications(), HttpStatus.OK);
    }

    // view application by searching the name
    @GetMapping("/search")
    public ResponseEntity<List<Application>> getApplicationsByName(@RequestParam String name) {
        List<Application> applications = service.getApplicationsByName(name);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable String id) {
        boolean deleted = service.deleteApplication(id);
        if (deleted) {
            return ResponseEntity.ok("Application cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        }
    }

    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<Application>>getApplicationByRoomId(@PathVariable String roomId) {
        return new ResponseEntity<>(service.getApplicationsByRoomId(roomId), HttpStatus.OK);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUserId(@PathVariable String userId) {
        List<Application> applications = service.getApplicationsByUserId(userId);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applicationId/{applicationId}")
    public ResponseEntity<List<Application>> getApplicationsByApplicationId(@PathVariable String applicationId) {
        return new ResponseEntity<>(service.getApplicationById(applicationId), HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> createApplication(@RequestBody Application.ApplicationRequest applicationRequest) {
        Application createdApplication = service.createApplication(applicationRequest);
        String message = "🎉 New application received for room: " + applicationRequest.getRoomId();
        notificationPublisher.sendNotification(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    @GetMapping("/status/{applicationId}")
    public HttpEntity<List<Application>> getApplicationStatus(@PathVariable String status) {
        return new ResponseEntity<>(service.getApplicationStatus(status),HttpStatus.OK);
    }


}
