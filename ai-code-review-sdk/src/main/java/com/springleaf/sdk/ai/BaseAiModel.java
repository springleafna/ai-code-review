package com.springleaf.sdk.ai;

import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.domain.dto.ChatCompletionRequestDTO;
import com.springleaf.sdk.domain.dto.ChatCompletionSyncResponseDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class BaseAiModel implements AiModel {

    protected String apiURL;
    protected String apiKey;
    protected String model;

    public BaseAiModel(String apiURL, String apiKey, String model) {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception {
        requestDTO.setModel(this.model);

        URL url = new URL(apiURL);
        apiKey = generateApiKey(apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = JSON.toJSONString(requestDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        return JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    }

    protected abstract String generateApiKey(String apiKey);
}
