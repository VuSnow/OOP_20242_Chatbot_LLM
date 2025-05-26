from fastapi import FastAPI, Request
from pydantic import BaseModel
from typing import List
import uvicorn
from generateTitle import generate_title
from chat_request import ChatRequest
from chat_message import ChatMessage
from google import genai
import os

from dotenv import load_dotenv
load_dotenv()

app = FastAPI()
client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

chat_sessions = {}

@app.post("/generate")
async def generate(request: ChatRequest):
    # print("Received request:", request)
    # print("Received prompt:", request.prompt)
    # print("Context:", [m.dict() for m in request.messages])
    # print("Model:", request.model)

    if(request.conversation_title == "new chat"):
        title = generate_title(request.prompt)
        print("Generated title:", title)

    else:
        title = request.conversation_title

    # Format messages for Gemini API
    contents = []
    for message in request.messages:
        if message.role == "user":
            contents.append({"role": "user", "parts": [{"text": message.content}]})
        else:
            contents.append({"role": "model", "parts": [{"text": message.content}]})
    print("Contents:", contents)
    
    response = client.models.generate_content(
        model="gemini-2.0-flash",
        contents=contents,
    )

    return {
        "answer": response.text,
        "title": title
    }

