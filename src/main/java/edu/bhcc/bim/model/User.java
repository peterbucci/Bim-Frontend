package edu.bhcc.bim.model;

public class User {
    private Integer userId;
    private String username;
    private UserStatus userStatus;
    private String lastActive;
    private Friendship friendship;

    public User() {
    }

    public User(Integer userId, String username, UserStatus userStatus, String lastActive) {
        this(userId, username, userStatus, lastActive, null);
    }

    public User(Integer userId, String username, UserStatus userStatus, String lastActive, Friendship friendship) {
        this.userId = userId;
        this.username = username;
        this.userStatus = userStatus;
        this.lastActive = lastActive;
        this.friendship = friendship;
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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }

    public enum Status {
        ONLINE, OFFLINE, AWAY, BUSY
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", lastActive='" + lastActive + '\'' +
                ", friendship=" + friendship +
                '}';
    }
}
