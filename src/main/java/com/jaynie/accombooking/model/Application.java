package com.jaynie.accombooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    private String userId;
    private String roomId;
    private String status;
    private String name;
    private String email;
    private String applicationId;
    private String applicationDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static class ApplicationRequest{
        private String roomId;
        private String userId;
        private String name;
        private String email;

        public String getRoomId() { return roomId; }
        public void setRoomId(String roomId) { this.roomId = roomId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

    }
}

