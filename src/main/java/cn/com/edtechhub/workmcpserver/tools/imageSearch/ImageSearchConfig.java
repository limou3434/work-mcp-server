package cn.com.edtechhub.workmcpserver.tools.imageSearch;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 图片搜索配置类
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Component
@ConfigurationProperties(prefix = "image-search")
@Data
@Slf4j
public class ImageSearchConfig {

    /**
     * Pexels 常规搜索接口密钥, <a href="https://www.pexels.com/zh-cn/api/key/">从官方文档中获取</a>
     */
    private String apiKey = "";

    /**
     * Pexels 常规搜索接口
     */
    private String apiUrl = "https://api.pexels.com/v1/search";

    /**
     * 打印配置
     */
    @PostConstruct
    public void printConfig() {
        log.debug("[{}] {}", this.getClass().getSimpleName(), this);
    }

}
