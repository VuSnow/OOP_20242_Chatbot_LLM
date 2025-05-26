package service.client;

public class LLMClientFactory {
	public static LLMClient getClient(String modelName) {
		switch(modelName.toLowerCase()) {
			case "gpt-3.5":
				return new GPT35Client();
			case "gpt-4":
				return new GPT4Client();
			case "local model":
				return new LocalModel();
			default:
				throw new IllegalArgumentException("Unknown model: " + modelName);
		}
	}
}
