package service.client;

import java.util.List;

import org.json.*;

import entity.conversation.Conversation;
import entity.message.ChatMessage;
import service.caller.LLMCaller;
import service.caller.OpenAiApiCaller;

public class GPT35Client implements LLMClient{
	
	private final LLMCaller caller = new OpenAiApiCaller("gpt-3.5");
	@Override
	public String generateResponse(String prompt, List<ChatMessage> context, Conversation conversation) {
		System.out.println("Generate Response for GPT 3.5 Model");
		return caller.call(prompt, context, conversation);
	}
	
	@Override
	public String getModelName() {
		return "GPT-3.5";
	}
}
