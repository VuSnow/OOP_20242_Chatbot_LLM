package caller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import entity.conversation.Conversation;
import entity.message.ChatMessage;

public class GeminiApiCaller implements LLMCaller{
	private static final String API_URL = "";
	private static final String API_KEY = "";
	
	private final String modelName;

    public GeminiApiCaller(String modelName) {
        this.modelName = modelName;
    }
	
	@Override
	public JSONObject call(String prompt, List<ChatMessage> context, Conversation conversation) {
		JSONObject payload = this.generateMsg(prompt, context, conversation);
		
//		System.out.println("Sending request with payload:");
//        System.out.println(payload.toString(2));
        
        try {
        	URL url = new URL("http://localhost:8000/generate");
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.setRequestMethod("POST");
        	conn.setRequestProperty("Content-Type", "application/json");
        	conn.setDoOutput(true);
        	
        	try (OutputStream os = conn.getOutputStream()){
        		byte[] input = payload.toString().getBytes("utf-8");
        		os.write(input, 0, input.length);
        	}
        	
        	StringBuilder response = new StringBuilder();
        	try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
        		String line;
        		while((line = br.readLine()) != null) {
        			response.append(line.trim());
        		}
        	}
        	
        	JSONObject responseJson = new JSONObject(response.toString());
//        	System.out.println(responseJson.toString());
        	return responseJson;
        	
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling LLM server", e);
        }
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
		payload.put("conversation_title", conversation.getTitle());
		payload.put("prompt", prompt);
		return payload;
	}
}
