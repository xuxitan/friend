package com.hiynn.friend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 极光推送配置信息 使用者自行配置并注入
 *
 * @author
 */
@Data
@Component
@ConfigurationProperties(prefix = "jpush")
public class JpushConfig {

    // 读取极光配置信息中的用户名密码
    private String appkey;

    private String masterSecret;

    private String liveTime;

}
