package com.springleaf.sdk.enumeration;

/**
 * Ai模型枚举类
 */
public enum AiModelEnum {

    DEEPSEEK_CHAT("deepseek-chat", "DeepSeek-V3模型"),
    DEEPSEEK_REASONER("deepseek-reasoner", "DeepSeek-R1模型");

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
