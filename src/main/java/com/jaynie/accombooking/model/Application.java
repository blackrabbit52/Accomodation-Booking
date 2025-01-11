package com.jaynie.accombooking.model;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    private String userId;
    private String roomId;
    private LocalDateTime appliedAt;
    private String status;
    private String name;
    private String email;
    private String applicationId;
    private String applicationDate;

    @Transient // Prevents this field from being stored in MongoDB
    private String roomName;

    @Transient
    private String location;

    @Transient
    private int Price_per_month_gbp;

    // Getters and Setters
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPrice_per_month_gbp() { return Price_per_month_gbp; }
    public void setPrice_per_month_gbp(int pricePerMonth) { this.Price_per_month_gbp = pricePerMonth; }




    public Application() {
        this.status = "Pending"; //  Default status when a new application is created
        this.appliedAt = LocalDateTime.now(); //  Automatically set application date
    }

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

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) {this.appliedAt = appliedAt;}

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
