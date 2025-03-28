import com.alibaba.fastjson2.JSON;
import com.springleaf.sdk.ai.AiModel;
import com.springleaf.sdk.ai.ChatGML;
import com.springleaf.sdk.domain.dto.ChatCompletionRequestDTO;
import com.springleaf.sdk.domain.dto.ChatCompletionSyncResponseDTO;
import com.springleaf.sdk.enumeration.AiModelEnum;
import com.springleaf.sdk.git.GitCommand;
import com.springleaf.sdk.utils.BearerTokenUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        String apiKey = "623611sadf3dff992c38b461e37cf26d.5jLZ2ByQLzSZHmZx";

        ChatCompletionRequestDTO chatCompletionRequestDTO = new ChatCompletionRequestDTO();
        chatCompletionRequestDTO.setModel(AiModelEnum.GLM_4.getCode());
        List<ChatCompletionRequestDTO.Prompt> messages = new ArrayList<>();
        messages.add(new ChatCompletionRequestDTO.Prompt("user", "你好，你是谁？"));
        chatCompletionRequestDTO.setMessages(messages);

        AiModel aiModel = new ChatGML(apiHost, apiKey);
        ChatCompletionSyncResponseDTO completions = aiModel.completions(chatCompletionRequestDTO);
        System.out.println(JSON.toJSONString(completions));
    }

}
