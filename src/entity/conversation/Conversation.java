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
    
    public Conversation(String id, int userId, String title, LocalDateTime createdTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdTime = createdTime;
        this.lastModifiedTime = createdTime;
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


    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
    	this.lastModifiedTime = lastModifiedTime;
    }

}
