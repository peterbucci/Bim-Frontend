package edu.bhcc.bim.model;

import java.sql.Timestamp;

public class Message {
    private Integer messageId;
    private Integer conversationId;
    private String content;
    private Timestamp sentAt;
    private String sender;
    private Integer senderId;

    public Message() {
    }

    public Message(Integer messageId, Integer conversationId, String content, Timestamp sentAt, String sender,
            Integer senderId) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.content = content;
        this.sentAt = sentAt;
        this.sender = sender;
        this.senderId = senderId;
    }

    // Getters and setters
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
}
