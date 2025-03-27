package com.springleaf.sdk.domain.entity;

/**
 * Ai提示词实体类
 */
public class Prompt {

    private String role;
    private String content;

    public Prompt() {
    }

    public Prompt(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
