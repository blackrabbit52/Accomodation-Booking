package com.jaynie.accombooking.service;

import com.jaynie.accombooking.model.Application;
import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.repositories.ApplicationRepository;
import com.jaynie.accombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private final ApplicationRepository applicationRepository;
    private final RoomRepository roomRepository ;

    public ApplicationService(RoomRepository roomRepository,ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
        this.roomRepository = roomRepository;
    }

    public List<Application> getAllApplications() {return applicationRepository.findAll();}
    public List<Application> getApplicationById(String id){ return applicationRepository.findByApplicationId(id);}
    public List<Application> getApplicationsByRoomId(String roomId) {return applicationRepository.findByRoomId(roomId);}
    public List<Application> getApplicationStatus(String status) {return applicationRepository.findByStatus(status);}
    public List<Application> getApplicationsByName(String name) {return applicationRepository.findByName(name);}

    public Application createApplication(Application.ApplicationRequest applicationRequest) {
        Room room = roomRepository.findById(applicationRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        // Check if the room is available
        if (!room.getAvailable()) {
            throw new IllegalStateException("Room is not available");
        }
        // Create a new application
        Application application = new Application();
        application.setRoomId(applicationRequest.getRoomId());
        application.setUserId(applicationRequest.getUserId());
        application.setName(applicationRequest.getName());
        application.setEmail(applicationRequest.getEmail());
        application.setStatus("Pending");
        application.setApplicationDate(String.valueOf(new Date()));
        application.setApplicationId("app-" + System.currentTimeMillis());
        //mark unavailabiltiy
        room.setAvailable(false);
        roomRepository.save(room);
        // Save
        return applicationRepository.save(application);
    }

    public boolean deleteApplication(String id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
        
    }

    public List<Application> getApplicationsByUserId(String userId) {
        List<Application> applications = applicationRepository.findByUserId(userId);

        for (Application application : applications) {
            // Fetch Room details
            Room room = roomRepository.findById(application.getRoomId()).orElse(null);

            if (room != null) {
                application.setRoomName(room.getName());
                application.setLocation(room.getLocation());
                application.setPrice_per_month_gbp(room.getPrice_per_month_gbp());
            }
        }

        return applications;
    }
}