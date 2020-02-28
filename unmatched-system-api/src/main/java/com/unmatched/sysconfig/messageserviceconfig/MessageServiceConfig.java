package com.unmatched.sysconfig.messageserviceconfig;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
* Description: 对Java异步消息的支持
* @author: yuhang tao
* @date: 2020/2/20
* @version: v1.0
*/
@Configuration
//@PropertySource("classpath:/properties/system.properties")
public class MessageServiceConfig {

    //设置activeMQ连接工厂(qa 环境下会生成)
    @Bean
    @Profile("qa")
    public ConnectionFactory connectionFactory(@Value("${activeMq_Url}") String url){
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(url);
        return activeMQConnectionFactory;
    }

    //申明activeMq消息目的地(队列或主题)
    //队列
    @Bean
    public Queue queue(){
        ActiveMQQueue queue=new ActiveMQQueue("messageQueue");
        return queue;
    }

    //主题
    @Bean
    public Topic topic(){
        ActiveMQTopic topic=new ActiveMQTopic("messageTopic");
        return topic;
    }

    //使用该Converter实现消息与json之间的相互转换
    @Bean
    public MessageConverter messageConverter(){
        //可以对该转换器进行配置，实现消息的细粒度控制，这里不做实现
        SimpleMessageConverter converter=new SimpleMessageConverter();
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,Queue queue,MessageConverter messageConverter){
        JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
        //设置默认的消息目的地是队列
        jmsTemplate.setDefaultDestination(queue);
        //jms默认的messageConverter是SimpleMessageConverter
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

    //JMS队列监听器容器工厂
    @Bean(name = "jmsQueueListenerCF")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置连接数
        factory.setConcurrency("3-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        return factory;
    }
}
