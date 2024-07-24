package edu.bhcc.bim.model;

public class User {
    private Integer userId;
    private String username;
    private String status;
    private String lastActive;

    // Constructor, getters, setters

    public User() {
    }

    public User(Integer userId, String username, String status, String lastActive) {
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.lastActive = lastActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }
}
