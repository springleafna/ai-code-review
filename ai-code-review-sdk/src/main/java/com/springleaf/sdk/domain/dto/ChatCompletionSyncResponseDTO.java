package com.springleaf.sdk.domain.dto;

import java.util.List;

/**
 * 调用Ai返回的json格式
 * "choices": [
 *       {
 *           "message": {
 *               "content": "以AI绘蓝图 — 智谱AI，让创新的每一刻成为可能。",
 *               "role": "assistant"
 *           }
 *       }
 *   ],
 */
public class ChatCompletionSyncResponseDTO {

    private List<Choice> choices;

    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public static class Message {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
