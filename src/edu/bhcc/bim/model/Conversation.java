package edu.bhcc.bim.model;

import java.util.List;

public class Conversation {
    private Integer conversationId;
    private User participant;
    private List<Message> messages;

    // Constructor, getters, setters

    public Conversation() {
    }

    public Conversation(Integer conversationId, ConversationType type,
            User participant, List<Message> messages) {
        this.conversationId = conversationId;
        this.participant = participant;
        this.messages = messages;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public enum ConversationType {
        GROUP, ONE_ON_ONE
    }
}
