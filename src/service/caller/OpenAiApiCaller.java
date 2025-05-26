package service.caller;

import java.util.List;

import org.json.JSONObject;

import entity.conversation.Conversation;
import entity.message.ChatMessage;

public class OpenAiApiCaller implements LLMCaller{
	
	private final String modelName;

    public OpenAiApiCaller(String modelName) {
        this.modelName = modelName;
    }
    
	@Override
	public JSONObject call(String prompt, List<ChatMessage> context, Conversation conversation) {
		return null;
	}
	
	@Override
	public JSONObject generateMsg(String prompt, List<ChatMessage> context, Conversation conversation) {
		return null;
	}
}
