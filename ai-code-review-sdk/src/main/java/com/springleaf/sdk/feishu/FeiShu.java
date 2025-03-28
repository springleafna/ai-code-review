package com.springleaf.sdk.feishu;

import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.domain.dto.FeiShuTemplateMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FeiShu {
    private final Logger logger = LoggerFactory.getLogger(FeiShu.class);

    private String webhook;

    public void sendTemplateMessage(String webhook, String logUrl) throws IOException {
        URL url = new URL(webhook);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        FeiShuTemplateMessageDTO feiShuTemplateMessageDTO = new FeiShuTemplateMessageDTO(
                "text",
                new FeiShuTemplateMessageDTO.Content(
                        "代码评审完成，地址为：" + logUrl
                ));

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = JSON.toJSONString(feiShuTemplateMessageDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("openai-code-review feiShu template message! {}", response);
        }
    }

    public FeiShu(String webhook) {
        this.webhook = webhook;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }
}
