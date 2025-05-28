package com.springleaf.sdk.enumeration;

import lombok.Getter;

/**
 * 通知消息模板枚举
 */
@Getter
public enum TemplateMessageEnum {
    REPO_NAME("repo_name","项目名称"),
    BRANCH_NAME("branch_name","分支名称"),
    COMMIT_AUTHOR("commit_author","提交者"),
    COMMIT_MESSAGE("commit_message","提交信息"),
    ;

    private final String code;
    private final String desc;

    TemplateMessageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
