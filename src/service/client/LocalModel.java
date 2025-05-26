package service.client;

import java.util.List;

import org.json.JSONObject;

import entity.conversation.Conversation;
import entity.message.ChatMessage;

public class LocalModel implements LLMClient{
	@Override
    public String generateResponse(String prompt, List<ChatMessage> context, Conversation conversation) {
        // Gọi API OpenAI GPT-4
		System.out.println("Generate Response for Local Model");
        return null;
    }

    @Override
    public String getModelName() {
        return "Local Model";
    }
}
