package edu.bhcc.bim.model;

public class Friendship {
    private Integer fromUserId;
    private Integer toUserId;
    private Status status;

    public Friendship() {
    }

    public Friendship(Integer fromUserId, Integer toUserId, Status status) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = status;
    }

    // Getters and setters
    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PENDING, ACCEPTED, BLOCKED
    }
}
