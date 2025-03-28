package com.springleaf.sdk.enumeration;

/**
 * Ai模型枚举类
 */
public enum AiModelEnum {

    DEEPSEEK_CHAT("deepseek-chat", "DeepSeek-V3模型"),
    DEEPSEEK_REASONER("deepseek-reasoner", "DeepSeek-R1模型"),
    GLM_4("glm-4","适用于复杂的对话交互和深度内容创作设计的场景"),
    GLM_4V("glm-4v","根据输入的自然语言指令和图像信息完成任务，推荐使用 SSE 或同步调用方式请求接口"),;

    private final String code;

    private final String info;

    AiModelEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
