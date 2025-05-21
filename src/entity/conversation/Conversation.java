package entity.conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.message.ChatMessage;

public class Conversation {
    private final String id;
    private final LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private final int userId;
    private String title;
    private final List<ChatMessage> messages;
    private boolean isArchived;

    public Conversation(String id, int userId, String title, LocalDateTime createdTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdTime = createdTime;
        this.lastModifiedTime = createdTime;
        this.messages = new ArrayList<>();
        this.isArchived = false;
    }

    // Getters
    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }

    // Business logic
    public void addMessage(ChatMessage message) {
        messages.add(message);
        lastModifiedTime = LocalDateTime.now();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public ChatMessage getLastMessage() {
        return isEmpty() ? null : messages.get(messages.size() - 1);
    }
}
