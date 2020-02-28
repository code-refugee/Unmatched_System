package com.unmatched.controller;

import com.unmatched.pojo.User;
import com.unmatched.service.SimpleJdbcTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/queryData")
public class QueryDataController {

    @Autowired
    private SimpleJdbcTemplateService simpleJdbcTemplateService;

    @RequestMapping("/queryStepInfo")
    @ResponseBody
    public String queryStepInfo(){
        return simpleJdbcTemplateService.queryStepInfo();
    }

    @RequestMapping("/queryUserInfo")
    @ResponseBody
    public String queryUserInfo(){
        return simpleJdbcTemplateService.queryUserInfo("张三");
    }

}
