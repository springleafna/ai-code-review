package com.springleaf.sdk.git;

import com.springleaf.sdk.utils.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Git 命令封装类
 * 获取git diff结果、提交代码审查建议并推送到GitHub仓库
 */
public class GitCommand {

    private final Logger logger = LoggerFactory.getLogger(GitCommand.class);

    /**
     * github评审日志仓库地址
     */
    private final String githubReviewLogUri;

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

    public GitCommand(String githubReviewLogUri, String githubToken, String project, String branch, String author, String message) {
        this.githubReviewLogUri = githubReviewLogUri;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    public String getProject() {
        return project;
    }

    public String getBranch() {
        return branch;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 获取代码差异：获取当前目录中最近一次提交与前一个提交之间的差异。 git diff 命令的输出结果。
     * @return 代码差异内容
     */
    public String diff() throws IOException, InterruptedException {
        // 检查是否安装 Git
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "--version");
            pb.start().waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Git 未安装或配置错误");
        }

        // 获取最新提交与前一个提交之间的差异
        Process diffProcess = getDiffProcess();

        StringBuilder diffCode = new StringBuilder();
        BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()));
        String line;
        while ((line = diffReader.readLine()) != null) {
            diffCode.append(line).append("\n");
        }
        diffReader.close();

        int exitCode = diffProcess.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to get diff, exit code:" + exitCode);
        }

        // 返回git diff命令的输出结果
        return diffCode.toString();
    }

    /**
     * 获取最新提交与前一个提交之间的差异的外部 Git 进程。
     */
    private static Process getDiffProcess() throws IOException, InterruptedException {
        // 使用 git log -1 --pretty=format:%H 命令获取最新提交的哈希值
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        // 设置工作目录在当前目录，即 git 命令执行的目录（\ai-code-review\下）
        logProcessBuilder.directory(new File("."));
        // 启动外部 Git 进程
        Process logProcess = logProcessBuilder.start();
        // getInputStream()：获取命令的标准输出流（即 git log 打印的哈希值）。
        //通过 BufferedReader 读取输出流的第一行（即哈希值）。
        BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
        // git log -1 --pretty=format:%H 的输出只有一行（哈希值），所以直接 readLine() 即可
        String latestCommitHash = logReader.readLine();
        logReader.close();
        logProcess.waitFor();

        // 使用 git diff 命令获取最新提交与前一个提交之间的差异
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        diffProcessBuilder.directory(new File("."));
        return diffProcessBuilder.start();
    }

    /**
     * 提交并推送代码审查建议：将代码审查建议提交并推送到指定的 GitHub 仓库，并返回新文件的 URL(github 记录评审内容 的仓库文件地址)。
     * @return github 记录评审内容 的仓库文件地址
     */
    public String commitAndPush(String recommend) throws Exception {
        Git git = Git.cloneRepository()
                .setURI(githubReviewLogUri + ".git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""))
                .call();

        // 创建分支
        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }

        String fileName = project + "-" + branch + "-" + author + System.currentTimeMillis() + "-" + RandomStringUtils.randomNumeric(4) + ".md";
        File newFile = new File(dateFolder, fileName);
        try (FileWriter writer = new FileWriter(newFile)) {
            writer.write(recommend);
        }

        // 提交内容
        git.add().addFilepattern(dateFolderName + "/" + fileName).call();
        git.commit().setMessage("add code review new file" + fileName).call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, "")).call();

        logger.info("ai-code-review git commit and push done! {}", fileName);

        return githubReviewLogUri + "/blob/master/" + dateFolderName + "/" + fileName;
    }

}
