package com.jaynie.accombooking.service;

import com.jaynie.accombooking.model.Application;
import com.jaynie.accombooking.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> getAllApplications() { return applicationRepository.findAll();}
    public List<Application> getApplicationById(String id){ return applicationRepository.findByUserId(id);}
    public Application createApplication(Application application) {return applicationRepository.save(application);}
    public List<Application> getApplicationsByUserId(String userId) {return applicationRepository.findByUserId(userId);}
    public List<Application> getApplicationsByRoomId(String roomId) {return applicationRepository.findByRoomId(roomId);}
    public void deleteApplication(String id) {applicationRepository.deleteById(id);}




}
