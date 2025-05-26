package service.client;

public class LLMClientFactory {
	public static LLMClient getClient(String modelName) {
		switch(modelName.toLowerCase()) {
			case "gemini-2.0-flash":
				return new GeminiClient();
			case "gpt-4":
				return new GPT4Client();
			case "local model":
				return new LocalModel();
			default:
				throw new IllegalArgumentException("Unknown model: " + modelName);
		}
	}
}
