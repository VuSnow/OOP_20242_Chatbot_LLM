package client;

import java.util.List;

import org.json.*;

import caller.GeminiApiCaller;
import caller.LLMCaller;
import entity.conversation.Conversation;
import entity.message.ChatMessage;

public class GeminiClient implements LLMClient{
	
	private final LLMCaller caller = new GeminiApiCaller("gemini-2.0-flash");
	@Override
	public JSONObject generateResponse(String prompt, List<ChatMessage> context, Conversation conversation) {
		System.out.println("Generate Response for Gemini Model");
		return caller.call(prompt, context, conversation);
	}
	
	@Override
	public String getModelName() {
		return "GPT-3.5";
	}
}
