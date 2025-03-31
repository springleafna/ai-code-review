package com.springleaf.sdk.service;

import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.feishu.FeiShu;
import com.springleaf.sdk.git.GitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractAiCodeReviewService implements AiCodeReviewService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected GitCommand gitCommand;
    protected AiModel aiModel;
    protected FeiShu feiShu;

    protected AbstractAiCodeReviewService(GitCommand gitCommand, AiModel aiModel, FeiShu feiShu) {
        this.gitCommand = gitCommand;
        this.aiModel = aiModel;
        this.feiShu = feiShu;
    }


    @Override
    public void exec() {
        try  {
            // 1.获取提交代码
            String diffCode = getDiffCode();
            // 2.调用AI模型进行代码评审：获取评审结果
            String recommend = codeReview(diffCode);
            // 3.记录评审结果到github仓库：返回github日志地址
            String logUrl = recordCodeReview(recommend);
            // 4.发送消息通知到微信公众号：日志地址、评审结果
            pushMessage(logUrl);
        } catch (Exception e) {
            logger.error("ai code review error", e);
        }
    }

    protected abstract String getDiffCode() throws IOException, InterruptedException;

    protected abstract String codeReview(String diffCode) throws Exception;

    protected abstract String recordCodeReview(String recommend) throws Exception;

    protected abstract void pushMessage(String logUrl) throws IOException;
}
