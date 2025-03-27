import com.springleaf.sdk.git.GitCommand;
import org.junit.Test;

import java.io.IOException;

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
}
