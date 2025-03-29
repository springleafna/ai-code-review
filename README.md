# jar包使用说明
1. 在项目工程下添加GitHub Actions，并配置相关的工作流:  
xxxProject/.github/workflows/xxx.yml:  
```yml
name: Build and Run AiCodeReview By Main Maven Jar

# 当代码被推送到master分支，或者有拉取请求指向master分支时，工作流会被触发。
on:
  push:
    branches:
      - master
  pull_request:
    branches:
      - master

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v2
        with:
          fetch-depth: 2

      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          distribution: 'adopt'
          java-version: '11'

      - name: Create libs directory
        run: mkdir -p ./libs

      - name: Download openai-code-review-sdk JAR
        run: wget -O ./libs/ai-code-review-sdk-1.0.jar https://github.com/springleafna/ai-code-review/releases/download/v1.0/ai-code-review-sdk-1.0.jar

      - name: Get repository name
        id: repo-name
        run: echo "REPO_NAME=${GITHUB_REPOSITORY##*/}" >> $GITHUB_ENV

      - name: Get branch name
        id: branch-name
        run: echo "BRANCH_NAME=${GITHUB_REF#refs/heads/}" >> $GITHUB_ENV

      - name: Get commit author
        id: commit-author
        run: echo "COMMIT_AUTHOR=$(git log -1 --pretty=format:'%an <%ae>')" >> $GITHUB_ENV

      - name: Get commit message
        id: commit-message
        run: echo "COMMIT_MESSAGE=$(git log -1 --pretty=format:'%s')" >> $GITHUB_ENV

      - name: Print repository, branch name, commit author, and commit message
        run: |
          echo "Repository name is ${{ env.REPO_NAME }}"
          echo "Branch name is ${{ env.BRANCH_NAME }}"
          echo "Commit author is ${{ env.COMMIT_AUTHOR }}"
          echo "Commit message is ${{ env.COMMIT_MESSAGE }}"      

      - name: Run Code Review
        run: java -jar ./libs/ai-code-review-sdk-1.0.jar
        env:
          # GitHub 配置；GITHUB_REVIEW_LOG_URI「https://github.com/xfg-studio-project/openai-code-review-log」、GITHUB_TOKEN「https://github.com/settings/tokens」
          GITHUB_REVIEW_LOG_URI: ${{ secrets.CODE_REVIEW_LOG_URI }}
          GITHUB_TOKEN: ${{ secrets.CODE_TOKEN }}
          COMMIT_PROJECT: ${{ env.REPO_NAME }}
          COMMIT_BRANCH: ${{ env.BRANCH_NAME }}
          COMMIT_AUTHOR: ${{ env.COMMIT_AUTHOR }}
          COMMIT_MESSAGE: ${{ env.COMMIT_MESSAGE }}
          # Ai - ChatGLM 配置「https://open.bigmodel.cn/api/paas/v4/chat/completions」、「https://open.bigmodel.cn/usercenter/apikeys」
          CHATGLM_APIKEY: ${{ secrets.CHATGLM_APIKEY }}
          # Ai - DeepSeek 配置
          DEEPSEEK_APIKEY: ${{ secrets.DEEPSEEK_APIKEY }}
          # 飞书Webhook地址
          FEISHU_WEBHOOK: ${{ secrets.FEISHU_WEBHOOK }}
```
2. 在GitHub项目工程仓库里添加GitHub Secrets，并配置相关的密钥:  
- CODE_REVIEW_LOG_URI: 代码审查日志存储仓库地址，如：https://github.com/xxx/ai-code-review-log
- CODE_TOKEN: GitHub Token，用于鉴权
- DEEPSEEK_APIKEY: DeepSeek API Key，用于调用DeepSeek接口进行代码评审。
- FEISHU_WEBHOOK: 飞书机器人 Webhook，用于飞书机器人消息推送。


## ProcessBuilder
是 Java 中用于启动和管理外部进程的类。
ProcessBuilder类是J2SE 1.5在java.lang中新添加的一个新类，此类用于创建操作系统进程，它提供一种启动和管理进程（也就是应用程序）的方法。  
在J2SE 1.5之前，都是由Process类处来实现进程的控制管理。每个 ProcessBuilder 实例管理一个进程属性集。  
它的start() 方法利用这些属性创建一个新的 Process 实例。start() 方法可以从同一实例重复调用，以利用相同的或相关的属性创建新的子进程。  
常用方法：  
- void command(String... command) 用于将待执行命令及参数传递给它；  

- Process start() 执行命令并返回一个Process对象，用于获取对执行程序的输入和输出；  

- void directory(File base) 用于设置待执行命令的工作目录，可以不设置；  

它可以：  
- 执行系统命令（如 git、ls 等）。  
- 设置命令的工作目录、环境变量。  
- 重定向输入/输出流。  
- 等待进程执行完成。  

为什么用 ProcessBuilder 而不是直接 Runtime.exec()？  
- 更安全的参数处理（避免 Shell 注入风险）。  
- 更灵活的重定向和目录控制。  
- 更清晰的 API 设计。  

## 智谱AiHTTP调用
HTTP 用户鉴权  
在调用模型接口时，支持两种鉴权方式：

传 API Key 进行认证
传鉴权 token 进行认证
新版机制中平台颁发的 API Key 同时包含 “用户标识 id” 和 “签名密钥 secret”，即格式为 {id}.{secret}

使用 JWT 组装 Token 后进行请求  
用户端需引入对应 JWT 相关工具类，并按以下方式组装 JWT 中 header、payload 部分  
1、header 具体示例  
```{"alg":"HS256","sign_type":"SIGN"}```  
- alg : 属性表示签名使用的算法，默认为 HMAC SHA256（写为HS256）  
- sign_type : 属性表示令牌的类型，JWT 令牌统一写为 SIGN 。  

2、payload 具体示例  
```{"api_key":{ApiKey.id},"exp":1682503829130, "timestamp":1682503820130}```  
- api_key : 属性表示用户标识 id，即用户API Key的{id}部分  
- exp : 属性表示生成的JWT的过期时间，客户端控制，单位为毫秒  
- timestamp : 属性表示当前时间戳，单位为毫秒  

## 使用 HttpURLConnection 发送 POST 请求
在 Java 编程中，HttpURLConnection 类是一个用于发送 HTTP 请求的工具。当需要通过 POST 方法发送 JSON 数据时，可以按照以下步骤进行：
### 创建 URL 对象
首先，创建一个 URL 对象，指向接受 JSON 数据的目标 URI。例如：  
``URL url = new URL("https://reqres.in/api/users");``  
### 打开连接
使用 URL 对象的 openConnection 方法来获取 HttpURLConnection 对象。由于 HttpURLConnection 是一个抽象类，因此不能直接实例化：  
``HttpURLConnection con = (HttpURLConnection) url.openConnection();``  `
### 设置请求方法
为了发送 POST 请求，需要将请求方法设置为 POST：
``con.setRequestMethod("POST");``  
### 设置请求头参数
设置请求头中的 "Content-Type" 为 "application/json"，这样可以将请求内容以 JSON 形式发送。如果不这样做，服务器可能会返回 HTTP 状态码 "400-bad request"：
``con.setRequestProperty("Content-Type", "application/json");``  
### 设置响应格式
设置请求头中的 "Accept" 为 "application/json"，以便以所需格式读取响应：  
``con.setRequestProperty("Accept", "application/json");``  
### 启用输出
为了发送请求内容，需要将 URLConnection 对象的 doOutput 属性设置为 true。否则，将无法将内容写入连接的输出流：  
``con.setDoOutput(true);``  
### 创建请求体
创建一个自定义的 JSON 字符串，然后将其写入输出流：  
```
String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";
try (OutputStream os = con.getOutputStream()) {
    byte[] input = jsonInputString.getBytes("utf-8");
    os.write(input, 0, input.length);
}
```
### 读取响应
从输入流中获取数据以读取响应内容。记得使用 try-with-resources 自动关闭响应流。读取整个响应内容，并打印最终的响应字符串：  
```
try (BufferedReader br = new BufferedReader(
    new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        String responseLine = null;
    while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
    }
System.out.println(response.toString());
}
```
如果响应是 JSON 格式的，可以使用第三方 JSON 解析器，如 Jackson 库、Gson 或 org.json 来解析响应。