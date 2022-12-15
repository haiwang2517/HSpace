# MQTT Spring Boot 集成
## 背景
&nbsp;&nbsp;&nbsp;&nbsp;最近IOT相关计算话题度比较高, 跟风了解下springboot怎么集成MQTT进行消息通信。废话不多说,开整。
### 目录
1. MQTT是什么,为什么需要MQTT
2. 本地搭建MQTT Broker服务(,有需要可以配置成集群)
3. springboot 怎么集成MQTT

## MQTT是什么,为什么需要MQTT
&nbsp;&nbsp;&nbsp;&nbsp;MQTT 
是一种基于发布/订阅模式的轻量级消息传输协议，专门针对低带宽和不稳定网络环境的物联网应用而设计，可以用极少的代码为联网设备提供实时可靠的消息服务。MQTT 协议广泛应用于物联网、移动互联网、智能硬件、车联网、智慧城市、远程医疗、电力、石油与能源等领域。    
&nbsp;&nbsp;&nbsp;&nbsp;MQTT 
协议根据主题而不是消息内容来分发消息，每个消息都包含一个主题，代理无需解析用户数据，这为实现一个通用的、与业务无关的 MQTT 
代理提供了可能。用户也可以随意对自己的数据进行加密，这对于广域网通信是非常有用的。    
    
**特性**
1. MQTT更加简单
2. MQTT网络更加稳定
3. 轻量级
4. 易于实现
5. 开放性


> [物联网首选协议，关于 MQTT 你需要了解这些](https://www.emqx.com/zh/blog/what-is-the-mqtt-protocol)

## 本地搭建MQTT Broker服务
[本地简单部署使用Docker体验](https://www.emqx.io/zh/downloads?os=Docker)


## springboot 怎么集成MQTT
### 引入依赖包
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-integration</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.integration</groupId>
        <artifactId>spring-integration-stream</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.integration</groupId>
        <artifactId>spring-integration-mqtt</artifactId>
    </dependency>
</dependencies>
```

### 代码配置
**添加配置文件application.yml**     
```yaml
hspace:
  mqtt:
    host: tcp://127.0.0.1:1883
    user-name: client
    password: 123
    qos: 1
    client-id: mqtt_client_demo
    timeout: 100000
    keepalive: 20
    default-topic: hspace/default
```
**MqttPropertiesConfig**    
```java
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
```

**MqttConfiguration**     
```java
@Configuration
@AllArgsConstructor
public class MqttConfiguration {
    MqttPropertiesConfig mqttPropertiesConfig;

    MqttGateway mqttGateway;

    /**
     * 设置MQTT连接默认配置
     * @return
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttPropertiesConfig.getUsername());
        mqttConnectOptions.setPassword(mqttPropertiesConfig.getPassword().toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{mqttPropertiesConfig.getHost()});
        mqttConnectOptions.setKeepAliveInterval(2);
        mqttConnectOptions.setKeepAliveInterval(mqttPropertiesConfig.getKeepalive());
        return mqttConnectOptions;
    }

    /**
     * 设置MQTT 客户端连接参数
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * 设置输入渠道
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 设置输出渠道
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 配置client,监听的topic
     */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttPropertiesConfig.getClientId(), mqttClientFactory(),
                        mqttPropertiesConfig.getDefaultTopic().split(","));
        adapter.setCompletionTimeout(mqttPropertiesConfig.getTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttPropertiesConfig.getQos());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String qos = message.getHeaders().get("mqtt_receivedQos").toString();
                //触发事件 这里不再做业务处理，包 listener中做处理
                Object payload = message.getPayload();
                System.out.println("topic:["+ topic +"] message:" + payload.toString());

                // 业务处理完主动上报
                mqttGateway.sendToMqtt("主动上报"+ payload.toString());
                mqttGateway.sendToMqtt("ken/test/local", payload.toString());
            }
        };
    }


    /**
     * 发送消息和消费消息Channel可以使用相同MqttPahoClientFactory
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        // 在这里进行mqttOutboundChannel的相关设置
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(mqttPropertiesConfig.getClientId(), mqttClientFactory());
        // 如果设置成true，发送消息时将不会阻塞。
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttPropertiesConfig.getDefaultTopic());
        return messageHandler;
    }
}

```

[spring integration](https://docs.spring.io/spring-integration/docs/current/reference/html/mqtt.html#mqtt)







