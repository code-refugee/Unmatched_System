package com.unmatched.service.impl;

import com.unmatched.service.RmiService;
import org.springframework.stereotype.Service;

//远程服务调用测试
@Service
public class RmiServiceImpl implements RmiService {
    @Override
    public void doSomeThing() {
        System.out.println("你调用了远程方法了！");
    }
}
