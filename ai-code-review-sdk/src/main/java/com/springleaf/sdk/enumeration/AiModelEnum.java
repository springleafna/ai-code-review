package com.springleaf.sdk.enumeration;

import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.ai.impl.ChatGML;
import com.springleaf.sdk.ai.impl.DeepSeek;

/**
 * Ai模型枚举类
 * 每个枚举常量都是枚举类的一个实例，而枚举类本身是一个继承自 java.lang.Enum 的类。
 * 当枚举类包含抽象方法时，这些匿名子类必须实现所有抽象方法，否则无法实例化。
 */
public enum AiModelEnum {

    DEEPSEEK_CHAT("deepseek-chat", "DeepSeek-V3模型", "https://api.deepseek.com/chat/completions") {
        @Override
        public AiModel createInstanceModel(String apiKey) {
            return new DeepSeek(this.getApiURL(), apiKey);
        }
    },
    DEEPSEEK_REASONER("deepseek-reasoner", "DeepSeek-R1模型", "https://api.deepseek.com/chat/completions") {
        @Override
        public AiModel createInstanceModel(String apiKey) {
            return new DeepSeek(this.getApiURL(), apiKey);
        }
    },
    GLM_4("glm-4","适用于复杂的对话交互和深度内容创作设计的场景", "https://open.bigmodel.cn/api/paas/v4/chat/completions") {
        @Override
        public AiModel createInstanceModel(String apiKey) {
            return new ChatGML(this.getApiURL(), apiKey);
        }
    },
    GLM_4V("glm-4v","根据输入的自然语言指令和图像信息完成任务，推荐使用 SSE 或同步调用方式请求接口", "https://open.bigmodel.cn/api/paas/v4/chat/completions") {
        @Override
        public AiModel createInstanceModel(String apiKey) {
            return new ChatGML(this.getApiURL(), apiKey);
        }
    };

    private final String code;

    private final String info;

    private final String apiURL;

    AiModelEnum(String code, String info, String apiURL) {
        this.code = code;
        this.info = info;
        this.apiURL = apiURL;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getApiURL() {
        return apiURL;
    }

    public abstract AiModel createInstanceModel(String apiKey);

}
