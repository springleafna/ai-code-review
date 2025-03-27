package com.springleaf.sdk.git;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitCommand {

    private final Logger logger = LoggerFactory.getLogger(GitCommand.class);

    /**
     * github仓库地址
     */
    private final String githubReviewUrl;

    /**
     * 用于身份验证的 GitHub 访问令牌
     */
    private final String githubToken;

    /**
     * 项目名称
     */
    private final String project;

    /**
     * 分支名称
     */
    private final String branch;

    /**
     * 代码提交者
     */
    private final String author;

    /**
     * 提交信息
     */
    private final String message;

    public GitCommand(String githubReviewUrl, String githubToken, String project, String branch, String author, String message) {
        this.githubReviewUrl = githubReviewUrl;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    /**
     * 获取代码差异：获取当前目录中最近一次提交与前一个提交之间的差异。
     * @return 代码差异内容
     */
    public String diff() {
        // 获取前一个提交的哈希值
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");


        return "";
    }

    /**
     * 提交并推送代码审查建议：将代码审查建议提交并推送到指定的 GitHub 仓库，并返回新文件的 URL(github 记录评审内容 的仓库文件地址)。
     * @return github 记录评审内容 的仓库文件地址
     */
    public String commitAndPush() {
        return "";
    }

}
