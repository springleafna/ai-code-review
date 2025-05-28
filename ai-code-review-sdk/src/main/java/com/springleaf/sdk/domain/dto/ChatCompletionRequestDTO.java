package com.springleaf.sdk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
public class ChatCompletionRequestDTO {

    private String model;
    private List<Prompt> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prompt {
        private String role;
        private String content;
    }
}
