package com.springleaf.sdk.ai.impl;

import com.springleaf.sdk.ai.BaseAiModel;

public class DeepSeek extends BaseAiModel {

    public DeepSeek(String apiURL, String apiKey, String model) {
        super(apiURL, apiKey, model);
    }

    @Override
    protected String generateApiKey(String apiKey) {
        return apiKey;
    }
}
