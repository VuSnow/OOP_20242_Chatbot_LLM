package service.caller;

import java.util.List;
import org.json.JSONObject;

import entity.conversation.Conversation;
import entity.message.ChatMessage;

public interface LLMCaller {
	String call(String prompt, List<ChatMessage> context, Conversation conversation);
	JSONObject generateMsg(String prompt, List<ChatMessage> context, Conversation conversation);
}
