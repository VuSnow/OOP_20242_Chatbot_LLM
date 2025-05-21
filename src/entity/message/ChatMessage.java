package entity.message;

import java.util.UUID;
import java.time.LocalDateTime;

public class ChatMessage {
	private final String id;
    private final String conversationId;
    private final int userId;
    private final String sender;
    private final String content;
    private final LocalDateTime timestamp;
    private final ChatMessageType type;

    public ChatMessage(String conversationId, int userId, String sender, String content,
                       LocalDateTime timestamp, ChatMessageType type) {
        this.id = UUID.randomUUID().toString();
        this.conversationId = conversationId;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
    }

	public ChatMessage(String id, String conversation_id, int userId, String sender, String content, LocalDateTime timeStamp) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.conversationId = conversation_id;
		this.userId = userId;
		this.sender = sender;
		this.content = content;
		this.timestamp = timeStamp;
		this.type = ChatMessageType.TEXT;
	}

	public String getId() {
		return id;
	}

	public String getConversationId() {
		return conversationId;
	}

	public String getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public ChatMessageType getType() {
		return type;
	}
    
	public int getUserId() {
		return userId;
	}

	// Convenience methods
    public boolean isFromUser() {
        return "user".equalsIgnoreCase(sender);
    }

    public boolean isFromAssistant() {
        return "assistant".equalsIgnoreCase(sender);
    }

    public boolean isText() {
        return type == ChatMessageType.TEXT;
    }

    public boolean isSystem() {
        return type == ChatMessageType.SYSTEM;
    }
    
}
