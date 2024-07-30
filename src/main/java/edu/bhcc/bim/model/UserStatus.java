package edu.bhcc.bim.model;

import java.sql.Timestamp;

public class UserStatus {
    private Integer id;
    private Integer userId;
    private User user;
    private Status status;
    private Timestamp lastActive;

    public UserStatus() {
    }

    public UserStatus(Integer userId, Status status) {
        this.userId = userId;
        this.status = status;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ONLINE, OFFLINE, AWAY, BUSY
    }

    public Timestamp getLastActive() {
        return lastActive;
    }

    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "userId=" + userId +
                ", status=" + status +
                '}';
    }
}
