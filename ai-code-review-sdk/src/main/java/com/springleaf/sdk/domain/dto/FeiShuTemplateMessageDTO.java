package com.springleaf.sdk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书通知，发起webhook通知时，需要传入的消息体
 * // 开启签名验证后发送文本消息
 * {
 *         "msg_type": "text",
 *         "content": {
 *                 "text": "request example"
 *         }
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeiShuTemplateMessageDTO {

    private String msg_type;
    private Content content;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content{
        private String text;
    }
}
