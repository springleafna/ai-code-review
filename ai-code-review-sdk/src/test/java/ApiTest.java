import com.alibaba.fastjson2.JSON;
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
    public void testChatWithZhiPu() throws IOException {
        String api = "623611edfa3dff99asdfb461e37cf26d.5jLZ2ByQLzSZHmZx";

        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + api);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);


    }

}
