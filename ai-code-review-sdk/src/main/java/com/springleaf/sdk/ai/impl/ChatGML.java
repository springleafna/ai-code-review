package com.springleaf.sdk.ai.impl;

import com.springleaf.sdk.ai.BaseAiModel;
import com.springleaf.sdk.utils.BearerTokenUtil;

public class ChatGML extends BaseAiModel {

    public ChatGML(String apiURL, String apiKey, String model) {
        super(apiURL, apiKey, model);
    }

    @Override
    protected String generateApiKey(String apiKey) {
        return BearerTokenUtil.getToken(apiKey);
    }
}
