package cn.com.edtechhub.workmcpserver;

import cn.com.edtechhub.workmcpserver.tools.imageSearch.ImageSearchTool;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpSyncServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动程序
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@SpringBootApplication
@Slf4j
public class WorkMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkMcpServerApplication.class, args);
    }

    /**
     * 注册 MCP 工具来暴露出去
     *
     * @param imageSearchTool 图片搜索工具实例
     * @return 工具调用提供者
     */
    @Bean
    public ToolCallbackProvider imageSearchTools(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }

}
