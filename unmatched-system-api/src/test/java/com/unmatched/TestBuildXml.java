package com.unmatched;

import com.unmatched.converter.CoreAcctRecordConverter;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import com.unmatched.sysconfig.baseconfig.WebRootConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebRootConfig.class})
public class TestBuildXml {

    private User user;

    private List<Step> steps;

    @Autowired
    private CoreAcctRecordConverter coreAcctRecordConverter;

    @Before
    public void before(){
        user=new User();
        user.setUsername("张三");
        user.setPassword("Q1876310z");
        steps=new ArrayList<>(10);
        for (int i=0;i<10;i++){
            Step step=new Step();
            step.setStepName("step_"+i);
            step.setStepIndex(i);
            steps.add(step);
        }
    }

    @Test
    public void testBuildXml(){
        System.out.println(coreAcctRecordConverter.encode(user,steps));
    }
}
