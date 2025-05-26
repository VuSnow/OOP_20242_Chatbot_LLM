package service.caller;

import java.util.List;

import org.json.JSONObject;

import entity.conversation.Conversation;
import entity.message.ChatMessage;

public class OpenAiApiCaller implements LLMCaller{
	private static final String API_URL = "";
	private static final String API_KEY = "";
	
	private final String modelName;

    public OpenAiApiCaller(String modelName) {
        this.modelName = modelName;
    }
	
	@Override
	public String call(String prompt, List<ChatMessage> context, Conversation conversation) {
		JSONObject payload = this.generateMsg(prompt, context, conversation);
		
		System.out.println("Sending request with payload:");
        System.out.println(payload.toString(2));

        return "Simulated response from " + this.modelName;
	}
	
	@Override
	public JSONObject generateMsg(String prompt, List<ChatMessage> context, Conversation conversation) {
		JSONObject payload = new JSONObject();
		payload.put("model", this.modelName);
		payload.put("messages", context.stream().map(msg -> {
            JSONObject obj = new JSONObject();
            obj.put("role", msg.getSender());
            obj.put("content", msg.getContent());
            return obj;
        }).toList());
		payload.put("conversation title", conversation.getTitle());
		payload.put("prompt", prompt);
		return payload;
	}
}
