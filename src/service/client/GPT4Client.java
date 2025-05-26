package service.client;

import java.util.List;

import org.json.*;

import entity.conversation.Conversation;
import entity.message.ChatMessage;
import service.caller.GeminiApiCaller;
import service.caller.LLMCaller;
import service.caller.OpenAiApiCaller;

public class GPT4Client implements LLMClient {
	
	private final LLMCaller caller = new OpenAiApiCaller("gpt-4");
	
    @Override
    public JSONObject generateResponse(String prompt, List<ChatMessage> context, Conversation conversation) {
        // Gọi API OpenAI GPT-4
    	System.out.println("Generate Response for GPT 4 Model");
        return caller.call(prompt, context, conversation);
    }

    @Override
    public String getModelName() {
        return "GPT-4";
    }
}
