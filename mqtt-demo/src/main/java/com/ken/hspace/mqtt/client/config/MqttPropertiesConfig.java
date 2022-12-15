package com.ken.hspace.mqtt.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT 配置文件
 */
@ConfigurationProperties("hspace.mqtt")
@Component
@Data
public class MqttPropertiesConfig {
    /**
     * 连接地址 broker URL
     */
    private String host;

    private String username;

    private String password;

    private String clientId;

    private String defaultTopic;

    private Integer qos;

    /**
     * 添加超时 单位秒
     */
    private Integer timeout;

    private Integer keepalive;
}
