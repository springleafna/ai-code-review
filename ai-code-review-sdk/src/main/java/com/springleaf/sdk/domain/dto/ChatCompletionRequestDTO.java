package com.springleaf.sdk.domain.dto;

import java.util.List;

/**
 * 发起ai调用请求的json格式
 * --data '{
 *     "model": "glm-4",
 *     "messages": [
 *         {
 *             "role": "user",
 *             "content": "你好"
 *         }
 *     ]
 * }'
 */
public class ChatCompletionRequestDTO {

    private String model;
    private List<Prompt> messages;

    public static class Prompt {
        private String role;
        private String content;

        public Prompt() {
        }

        public Prompt(String role, String content) {
            this.role = role;
            this.content = content;
        }

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Prompt> getMessages() {
        return messages;
    }

    public void setMessages(List<Prompt> messages) {
        this.messages = messages;
    }
}
