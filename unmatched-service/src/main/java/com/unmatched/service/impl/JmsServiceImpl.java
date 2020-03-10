package com.unmatched.service.impl;

import com.google.gson.Gson;
import com.unmatched.pojo.User;
import com.unmatched.service.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;

@Service
public class JmsServiceImpl implements JmsService {

    @Autowired
    private JmsOperations jmsOperations;

    //新增了一个角色，将他推给客户端
    @Override
    public void sendUserToClient() {
        Gson gson=new Gson();
        User user=new User();
        user.setUserName("张三");
        user.setPassWord("123456");
        user.setId(1);
        jmsOperations.convertAndSend(gson.toJson(user));
    }

    /**
    * Description: 异步获取消息队列中的消息
     * destination 队列地址
     * containerFactory 监听器容器工厂, 若存在2个以上的监听容器工厂,需进行指定
    * @date: 2020/2/26
    */
    @JmsListener(containerFactory = "jmsQueueListenerCF",destination = "messageQueue")
    @Override
    public void getInfoAsync(String msg) {
        System.out.println("从消息队列中得到消息了！！！"+msg);
    }
}
