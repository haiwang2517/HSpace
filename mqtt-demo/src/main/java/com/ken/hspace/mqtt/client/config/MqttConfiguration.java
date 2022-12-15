package com.ken.hspace.mqtt.client.config;

import com.ken.hspace.mqtt.client.server.MqttGateway;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * MQTT 启动相关初始化配置项
 */
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
//                eventPublisher.publishEvent(new MqttEvent(this,topic,message.getPayload().toString()));
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
