package com.unmatched.controller;

import com.unmatched.service.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jms")
public class JmsController {

    @Autowired
    private JmsOperations jmsOperations;

    @Autowired
    private JmsService jmsService;

    @RequestMapping(value = "/send",method = RequestMethod.GET)
    public void sendMessageToClient(){
        jmsService.sendUserToClient();
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String getMessageFromServer(){
        return (String) jmsOperations.receiveAndConvert();
    }
}
