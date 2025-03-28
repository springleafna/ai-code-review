package com.springleaf.sdk.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class GenSignUtil {
    /**
     * 生成带有时效性的签名
     * @param timestamp 时间戳（秒单位，需在 1 小时内）
     * @param secret    密钥字符串
     * @return Base64 编码的签名
     */
    public static String genSign(long timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        // 1. 校验时间戳有效性（误差不超过 1 小时）
        long currentSeconds = Instant.now().getEpochSecond();
        if (Math.abs(currentSeconds - timestamp) > 3600) {
            throw new IllegalArgumentException("Invalid timestamp: 时间戳已过期或超前");
        }

        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.getEncoder().encode(signData));
    }
}
