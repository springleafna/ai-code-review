import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.ai.impl.ChatGML;
import com.springleaf.sdk.ai.impl.DeepSeek;
import com.springleaf.sdk.domain.dto.ChatCompletionRequestDTO;
import com.springleaf.sdk.domain.dto.ChatCompletionSyncResponseDTO;
import com.springleaf.sdk.domain.dto.FeiShuTemplateMessageDTO;
import com.springleaf.sdk.enumeration.AiModelEnum;
import com.springleaf.sdk.feishu.FeiShu;
import com.springleaf.sdk.git.GitCommand;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiTest {

    /**
     * 测试diff方法 获取git diff结果
     */
    @Test
    public void testDiff() throws IOException, InterruptedException {
        GitCommand gitCommand = new GitCommand("https://github.com/springleaf/springleaf-framework", "githubToken", "project", "branch", "author", "message");
        String diff = gitCommand.diff();
        System.out.println(diff);
    }

    /**
     * 测试获取调用智谱Ai对话接口时需要的鉴权token
     */
    @Test
    public void testGetToken() {
        String apiKey = "623611asdf3dff992c38b461e37cf26d.5jLZ2ByQLzSZHmZx";
        String[] split = apiKey.split("\\.");
        for (String s : split) {
            System.out.println(s);
        }
    }

    /**
     * 测试发起http请求与智谱Ai对话
     */
    @Test
    public void testChatWithZhiPu() throws Exception {

        String apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
        String apiKey = "623611edfa3dfadfasc38b461e37cf26d.5jLZ2ByQLzSZHmZx";

        ChatCompletionRequestDTO chatCompletionRequestDTO = new ChatCompletionRequestDTO();
        chatCompletionRequestDTO.setModel(AiModelEnum.GLM_4.getCode());
        List<ChatCompletionRequestDTO.Prompt> messages = new ArrayList<>();
        messages.add(new ChatCompletionRequestDTO.Prompt("user", "你好，你是谁？"));
        chatCompletionRequestDTO.setMessages(messages);

        AiModel aiModel = new ChatGML(apiHost, apiKey, AiModelEnum.GLM_4.getCode());
        ChatCompletionSyncResponseDTO completions = aiModel.completions(chatCompletionRequestDTO);
        System.out.println(JSON.toJSONString(completions));
    }

    /**
     * 测试发起http请求与DeepSeekAi对话
     */
    @Test
    public void testChatWithDeepSeek() throws Exception {

        AiModel aiModel = AiModelEnum.DEEPSEEK_REASONER.createInstanceModel("sk-01db804e3b324d02ba7a34beaab9b126");

        ChatCompletionRequestDTO chatCompletionRequestDTO = new ChatCompletionRequestDTO();
        chatCompletionRequestDTO.setModel(aiModel.getModel());
        List<ChatCompletionRequestDTO.Prompt> messages = new ArrayList<>();
        messages.add(new ChatCompletionRequestDTO.Prompt("user", "你好，你是谁？"));
        chatCompletionRequestDTO.setMessages(messages);

        ChatCompletionSyncResponseDTO completions = aiModel.completions(chatCompletionRequestDTO);
        System.out.println(JSON.toJSONString(completions));
    }


    /**
     * 测试使用飞书webhook方式通知评审消息
     */
    @Test
    public void testFeiShuWebhook() throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        URL url = new URL("https://open.feishu.cn/open-apis/bot/v2/hook/asdfasfdfas8f715fbbb");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        FeiShuTemplateMessageDTO feiShuTemplateMessageDTO = new FeiShuTemplateMessageDTO(
                "text",
                new FeiShuTemplateMessageDTO.Content(
                        "测试飞书webhook！"
                ));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(JSON.toJSONString(feiShuTemplateMessageDTO).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println("POST request not successful");
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);
        }
    }

    @Test
    public void test() throws NoSuchAlgorithmException, InvalidKeyException {
        FeiShuTemplateMessageDTO feiShuTemplateMessageDTO = new FeiShuTemplateMessageDTO(
                "text",
                new FeiShuTemplateMessageDTO.Content(
                        "新消息！"
                ));
        System.out.println(JSON.toJSONString(feiShuTemplateMessageDTO));
    }

    @Test
    public void testFeishu() throws IOException {
        FeiShu feiShu = new FeiShu("https://open.feishu.cn/open-apis/bot/v2/hook/d28b081e-7a18-4b22-9d0c-1ea8f715fbbb");
        feiShu.sendTemplateMessage("https://www.baidu.com");
    }

    /**
     * 测试选择不同和ai模型
     */
    @Test
    public void testAiModel() throws Exception {
        // 1. 定义模型检查优先级顺序
        final AiModelEnum[] MODEL_PRIORITY = {
                AiModelEnum.DEEPSEEK_CHAT,
                AiModelEnum.DEEPSEEK_REASONER,
                AiModelEnum.GLM_4,
                AiModelEnum.GLM_4V
        };

        // 2. 查找第一个配置了环境变量的模型
        AiModelEnum selectedModel = null;
        String apiKey = null;

        for (AiModelEnum model : MODEL_PRIORITY) {
            apiKey = System.getenv(model.getCode());
            if (apiKey != null && !apiKey.isEmpty()) {
                selectedModel = model;
                break;
            }
        }

        // 3. 未找到有效模型配置时快速失败
        if (selectedModel == null) {
            throw new IllegalStateException("未找到任何有效的AI模型环境变量配置。请配置以下任意环境变量: "
                    + Arrays.stream(MODEL_PRIORITY).map(AiModelEnum::getCode).collect(Collectors.joining(", ")));
        }

        // 4. 通过工厂方法创建模型实例
        AiModel aiModel = selectedModel.createInstanceModel(apiKey);

        // 5. 执行请求
        System.out.println(aiModel);

    }


}
