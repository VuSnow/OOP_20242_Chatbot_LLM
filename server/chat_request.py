from pydantic import BaseModel
from typing import List
from chat_message import ChatMessage
class ChatRequest(BaseModel):
    messages: List[ChatMessage]
    model: str
    prompt: str
    conversation_title: str