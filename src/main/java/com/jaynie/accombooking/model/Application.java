package com.jaynie.accombooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    private String userId;
    private String roomId;
    private String status;
    private Date applicationDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }

}

