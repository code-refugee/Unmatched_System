package com.unmatched.sysconfig.messageserviceconfig;

import com.unmatched.service.RmiService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import java.util.ArrayList;
import java.util.List;

/**
* Description: 对Java异步消息的支持
* @author: yuhang tao
* @date: 2020/2/20
* @version: v1.0
*/
@Configuration
//@PropertySource("classpath:/properties/system.properties")
public class MessageServiceConfig {

    @Autowired
    private RmiService rmiService;

    //设置activeMQ连接工厂(qa 环境下会生成)
    @Bean
    @Profile("qa")
    public ConnectionFactory connectionFactory(@Value("${activeMq_Url}") String url){
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(url);
//        List<String> needTrustPackage=new ArrayList<>();
        //除了信任这个包，还要信任JmsInvokerServiceExporter不然远程方法调用会报错
//        needTrustPackage.add("com.unmatched.service");
//        activeMQConnectionFactory.setTrustedPackages(needTrustPackage);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    //设置rabbitMq连接工厂（qa环境下生效）
    @Bean
    @Profile("qa")
    public com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory(@Value("${rabbitMq_host}") String host,
                                                                         @Value("${rabbitMq_port}") int port){
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory=new com.rabbitmq.client.ConnectionFactory();
        rabbitConnectionFactory.setHost(host);
        rabbitConnectionFactory.setPort(port);
        rabbitConnectionFactory.setUsername("guest");
        rabbitConnectionFactory.setPassword("guest");
        return rabbitConnectionFactory;
    }


    //申明activeMq消息目的地(队列或主题)
    //这个队列用于传输json数据
    @Bean
    public Queue queue(){
        ActiveMQQueue queue=new ActiveMQQueue("messageQueue");
        return queue;
    }

    //这个队列用于远程方法调用
    @Bean
    public Queue queue2(){
        ActiveMQQueue queue2=new ActiveMQQueue("messageQueueForService");
        return queue2;
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

    //有多个相同类型的bean时（这里有多个queue），自动注入会根据名字去查找合适的bean
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,Queue queue,MessageConverter messageConverter){
        JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
        //设置默认的消息目的地是队列
        jmsTemplate.setDefaultDestination(queue);
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

    //将RmiService导出为远程服务，可以被其它系统向调用本地方法一样调用
    //如何监听对RmiService的调用，参考applicationContext-MQ.xml中的配置
    @Bean(name = "jmsISE")
    public JmsInvokerServiceExporter exporter(){
        JmsInvokerServiceExporter exporter=new JmsInvokerServiceExporter();
        exporter.setService(rmiService);
        exporter.setServiceInterface(RmiService.class);
        return exporter;
    }

    //使用远程方法代理，纵然本地没有实现RmiService接口，依旧可以自动注入并调用
    //这里我只是做个示例，因为我们已经实现了RmiService接口
    @Bean
    public JmsInvokerProxyFactoryBean jmsInvokerProxyFactoryBean(ConnectionFactory connectionFactory){
        JmsInvokerProxyFactoryBean proxyFactoryBean=new JmsInvokerProxyFactoryBean();
        proxyFactoryBean.setConnectionFactory(connectionFactory);
        proxyFactoryBean.setQueueName("messageQueueForService");
        proxyFactoryBean.setServiceInterface(RmiService.class);
        return proxyFactoryBean;
    }
}
