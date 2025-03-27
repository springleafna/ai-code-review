package com.springleaf.sdk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAiCodeReviewService implements AiCodeReviewService {

    private Logger logger = LoggerFactory.getLogger(getClass());

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

    protected abstract String getDiffCode();

    protected abstract String codeReview(String diffCode);

    protected abstract String recordCodeReview(String recommend);

    protected abstract void pushMessage(String logUrl);
}
