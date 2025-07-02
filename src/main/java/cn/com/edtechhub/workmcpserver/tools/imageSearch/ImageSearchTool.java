package cn.com.edtechhub.workmcpserver.tools.imageSearch;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片搜索工具
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Service
public class ImageSearchTool {

    /**
     * 注入 ImageSearchConfig 依赖
     */
    @Resource
    private ImageSearchConfig imageSearchConfig;

    /**
     * 搜索图片列表服务
     *
     * @param query 搜索关键词
     * @return 图片路径列表字符串(图片链接之间使用逗号隔开)
     */
    @Tool(name = "searchImage", description = "search image from web")
    public String searchImage(@ToolParam(description = "Search query keyword") String query) {

        // TODO: 可以自己在终端中输入 {"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"searchImage","arguments":{"query":"cat"}}} 来检查 MCP 客户端是否正常可用, 这里的 method 来源于 Spring AI 源代码中的 McpAsyncServer 类中这句 requestHandlers.put("tools/call", this.toolsCallRequestHandler());
        // 响应效果应该类似是这样的
        // {"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"searchImage","arguments":{"query":"cat"}}}
        // 2025-07-02T21:21:14.776+08:00 DEBUG 83352 --- [work-mcp-server] [oundedElastic-1] o.s.ai.tool.method.MethodToolCallback    : Starting execution of tool: searchImage
        // 2025-07-02T21:21:15.908+08:00 DEBUG 83352 --- [work-mcp-server] [oundedElastic-1] o.s.ai.tool.method.MethodToolCallback    : Successful execution of tool: searchImage
        // 2025-07-02T21:21:15.908+08:00 DEBUG 83352 --- [work-mcp-server] [oundedElastic-1] o.s.a.t.e.DefaultToolCallResultConverter : Converting tool result to JSON.
        // {"jsonrpc":"2.0","id":1,"result":{"content":[{"type":"text","type":"text","text":"\"https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/416160/pexels-photo-416160.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/1170986/pexels-photo-1170986.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/1741205/pexels-photo-1741205.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/57416/cat-sweet-kitty-animals-57416.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/2071873/pexels-photo-2071873.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/20787/pexels-photo.jpg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/1056251/pexels-photo-1056251.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/208984/pexels-photo-208984.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/69932/tabby-cat-close-up-portrait-69932.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/774731/pexels-photo-774731.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/1543793/pexels-photo-1543793.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/2194261/pexels-photo-2194261.jpeg?auto=compress&cs=tinysrgb&h=350,https://images.pexels.com/photos/127028/pexels-photo-127028.jpeg?auto=compress&cs=tinysrgb&h=350\""}],"isError":false}}

        try {
            return String.join(",", this.searchMediumImages(query));
        } catch (Exception e) {
            return "Error search image: " + e.getMessage();
        }
    }

    /**
     * 搜索中等尺寸的图片列表
     *
     * @param query 搜索关键词
     * @return 图片路径列表字符串(图片链接之间使用逗号隔开)
     */
    private List<String> searchMediumImages(String query) {
        // 设置请求头（包含API密钥）
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", imageSearchConfig.getApiKey());

        // 设置请求参数（仅包含query，可根据文档补充page、per_page等参数）
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        // 发送 GET 请求
        String response = HttpUtil.createGet(imageSearchConfig.getApiUrl())
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();

        // 解析响应JSON（假设响应结构包含"photos"数组，每个元素包含"medium"字段）
        return JSONUtil.parseObj(response)
                .getJSONArray("photos")
                .stream()
                .map(photoObj -> (JSONObject) photoObj)
                .map(photoObj -> photoObj.getJSONObject("src"))
                .map(photo -> photo.getStr("medium"))
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }

}
