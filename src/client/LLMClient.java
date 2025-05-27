package client;

import java.util.List;

import entity.conversation.Conversation;
import entity.message.*;
import org.json.*;

public interface LLMClient {
	JSONObject generateResponse(String prompt, List<ChatMessage> context, Conversation conversation);
	String getModelName();
}
