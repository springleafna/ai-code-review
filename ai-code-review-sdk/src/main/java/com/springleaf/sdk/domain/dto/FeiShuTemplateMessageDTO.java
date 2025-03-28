package com.springleaf.sdk.domain.dto;

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
public class FeiShuTemplateMessageDTO {

    private String msg_type;
    private Content content;

    public static class Content{
        private String text;

        public Content(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public FeiShuTemplateMessageDTO(String msg_type, Content content) {
        this.msg_type = msg_type;
        this.content = content;
    }

    public FeiShuTemplateMessageDTO() {}
}
