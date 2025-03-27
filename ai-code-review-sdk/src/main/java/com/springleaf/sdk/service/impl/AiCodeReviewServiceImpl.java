package com.springleaf.sdk.service.impl;

import com.springleaf.sdk.service.AbstractAiCodeReviewService;

public class AiCodeReviewServiceImpl extends AbstractAiCodeReviewService {
    @Override
    protected String getDiffCode() {
        return null;
    }

    @Override
    protected String codeReview(String diffCode) {
        return null;
    }

    @Override
    protected String recordCodeReview(String recommend) {
        return null;
    }

    @Override
    protected void pushMessage(String logUrl) {

    }
}
