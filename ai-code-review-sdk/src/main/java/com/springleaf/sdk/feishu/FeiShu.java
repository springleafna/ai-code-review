package com.springleaf.sdk.feishu;

import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.domain.entity.FeishuCodeReviewCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class FeiShu {
    private final Logger logger = LoggerFactory.getLogger(FeiShu.class);
    private final String webhook;

    public FeiShu(String webhook) {
        if (webhook == null || webhook.trim().isEmpty()) {
            throw new IllegalArgumentException("Webhook URL cannot be null or empty");
        }
        if (!webhook.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid webhook URL format");
        }
        this.webhook = webhook;
    }

    public void sendTemplateMessage(String project, String author, String commitMessage, String logUrl) throws IOException {
        Objects.requireNonNull(project, "Project name cannot be null");
        Objects.requireNonNull(author, "Author cannot be null");
        Objects.requireNonNull(logUrl, "Log URL cannot be null");

        URL url = new URL(webhook);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        FeishuCodeReviewCard message = new FeishuCodeReviewCard(project, author, commitMessage, logUrl);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to send FeiShu message. Response code: " + responseCode);
        }

        try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("FeiShu message sent successfully. Response: {}", response);
        }
    }
}
