package com.springleaf.sdk.domain.dto;

import lombok.Data;

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
@Data
public class ChatCompletionSyncResponseDTO {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
