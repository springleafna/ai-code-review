package com.springleaf.sdk.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 在使用HTTP调用ChatGML模型接口时，支持两种鉴权方式：（<a href="https://open.bigmodel.cn/dev/api/http-call/http-auth">...</a>）
 * 传 API Key 进行认证
 * 传鉴权 token 进行认证
 * 这里是生成 token 的工具类
 */
public class BearerTokenUtils {

    // 过期时间；默认30分钟
    private static final long expireMillis = 30 * 60 * 1000L;

    // 缓存服务
    public static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireMillis - (60 * 1000L), TimeUnit.MILLISECONDS)
            .build();

    public static String getToken(String apiKey) {
        // \\.的作用是告诉split方法按照实际的句号字符来分割字符串，而不是按照正则表达式中.所表示的任意单个字符来分割。
        String[] split = apiKey.split("\\.");
        return getToken(split[0], split[1]);
    }

    /**
     * 对 ApiKey 进行签名
     * 新版机制中平台颁发的 API Key 同时包含 “用户标识 id” 和 “签名密钥 secret”，即格式为 {id}.{secret}
     *
     * @param id    登录创建 ApiKey的前半部分
     * @param secret ApiKey的后半部分 828902ec516c45307619708d3e780ae1.w5eKiLvhnLP8MtIf 取 w5eKiLvhnLP8MtIf 使用
     * @return Token
     */
    public static String getToken(String id, String secret) {
        // 缓存Token
        String token = cache.getIfPresent(id);
        if (null != token) return token;
        // 创建Token
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", id);
        payload.put("exp", System.currentTimeMillis() + expireMillis);
        payload.put("timestamp", Calendar.getInstance().getTimeInMillis());

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("sign_type", "SIGN");

        token = JWT.create().withPayload(payload).withHeader(headerClaims).sign(algorithm);
        cache.put(id, token);
        return token;
    }

}
