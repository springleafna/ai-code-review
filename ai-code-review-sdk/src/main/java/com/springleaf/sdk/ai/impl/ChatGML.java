package com.springleaf.sdk.ai.impl;

import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.domain.dto.ChatCompletionRequestDTO;
import com.springleaf.sdk.domain.dto.ChatCompletionSyncResponseDTO;
import com.springleaf.sdk.utils.BearerTokenUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatGML implements AiModel {

    private final String apiURL;
    private final String apiKey;
    private final String model;

    public ChatGML(String apiURL, String apiKey, String model) {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception {
        // 根据APIKEY获取token
        String token = BearerTokenUtil.getToken(apiKey);

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        // 创建请求体，将json数据写入请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = JSON.toJSONString(requestDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应结果
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        // 关闭连接
        in.close();
        connection.disconnect();

        return JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    }

    @Override
    public String getModel() {
        return this.model;
    }
}
