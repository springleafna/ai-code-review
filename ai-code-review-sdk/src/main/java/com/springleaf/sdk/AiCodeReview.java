package com.springleaf.sdk;

import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.ai.DeepSeek;
import com.springleaf.sdk.enumeration.AiModelEnum;
import com.springleaf.sdk.feishu.FeiShu;
import com.springleaf.sdk.git.GitCommand;
import com.springleaf.sdk.service.AiCodeReviewService;
import com.springleaf.sdk.service.impl.AiCodeReviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        AiModel aiModel = new DeepSeek(
                AiModelEnum.DEEPSEEK_CHAT.getApiURL(),
                getEnv("DEEPSEEK_APIKEY")
        );

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
            throw new RuntimeException("value is null");
        }
        return value;
    }
}
