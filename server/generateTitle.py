from chat_request import ChatRequest
from chat_message import ChatMessage

from google import genai
from google.genai import types

client = genai.Client(api_key="AIzaSyDUkJe5lFz7yszFkwqm5HK8dhGXmqe5zo0")

def generate_title(prompt:str):
    prompt = (
        f"Dựa vào tin nhắn đầu tiên của cuộc trò chuyện dưới đây, hãy tạo một tiêu đề ngắn gọn, súc tích và mô tả chính xác nội dung của cuộc trò chuyện \n\n"
        f"Tin nhắn đầu tiên: \"{prompt}\" \n\n"
        f"Tiêu đề:"
    )
    response = client.models.generate_content(
        model="gemini-2.0-flash",
        contents=prompt,
        config=types.GenerateContentConfig(system_instruction="You are a helpful assistant that generates titles for conversations based on the first message."),
    )
    print(response.text)
    return response.text

