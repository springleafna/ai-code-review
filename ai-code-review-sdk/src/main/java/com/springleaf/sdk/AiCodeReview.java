package com.springleaf.sdk;

import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.enumeration.AiModelEnum;
import com.springleaf.sdk.feishu.FeiShu;
import com.springleaf.sdk.git.GitCommand;
import com.springleaf.sdk.service.AiCodeReviewService;
import com.springleaf.sdk.service.impl.AiCodeReviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class AiCodeReview {

    private static final Logger logger = LoggerFactory.getLogger(AiCodeReview.class);

    public static void main(String[] args) {
        GitCommand gitCommand = new GitCommand(
                getEnv("GITHUB_REVIEW_LOG_URI"),
                getEnv("GITHUB_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        AiModel aiModel = getAiModel();

        FeiShu feiShu = new FeiShu(
                getEnv("FEISHU_WEBHOOK")
        );

        AiCodeReviewService aiCodeReviewService = new AiCodeReviewServiceImpl(gitCommand, aiModel, feiShu);
        aiCodeReviewService.exec();

        logger.info("Code review completed successfully!");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException( key + ": value is null");
        }
        return value;
    }

    // 读取配置文件获取对应的Ai模型
    private static AiModel getAiModel() {
        // 1. 定义模型检查优先级顺序
        final AiModelEnum[] MODEL_PRIORITY = {
                AiModelEnum.DEEPSEEK_CHAT,
                AiModelEnum.DEEPSEEK_REASONER,
                AiModelEnum.GLM_4,
                AiModelEnum.GLM_4V
        };

        // 2. 查找第一个配置了环境变量的模型
        AiModelEnum selectedModel = null;
        String apiKey = null;

        for (AiModelEnum model : MODEL_PRIORITY) {
            apiKey = System.getenv(model.getCode().toUpperCase(Locale.ROOT).replace('-', '_'));
            if (apiKey != null && !apiKey.isEmpty()) {
                selectedModel = model;
                break;
            }
        }

        // 3. 未找到有效模型配置时快速失败
        if (selectedModel == null) {
            throw new IllegalStateException("未找到任何有效的AI模型环境变量配置。请配置以下任意环境变量: "
                    + Arrays.stream(MODEL_PRIORITY).map(AiModelEnum::getCode).collect(Collectors.joining(", ")));
        }

        // 4. 通过工厂方法创建模型实例
        return selectedModel.createInstanceModel(apiKey);
    }
}
