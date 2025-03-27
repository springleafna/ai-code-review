package com.springleaf.sdk.domain.dto;

import com.springleaf.sdk.domain.entity.Prompt;

import java.util.List;

/**
 * 聊天完成请求 DTO
 */
public class ChatCompletionRequestDTO {

    private String model;
    private List<Prompt> messages;

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
