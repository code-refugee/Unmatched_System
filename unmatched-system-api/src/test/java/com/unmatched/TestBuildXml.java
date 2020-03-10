package com.unmatched;

import com.unmatched.converter.CoreAcctRecordConverter;
import com.unmatched.pojo.Loop;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import com.unmatched.sysconfig.baseconfig.WebRootConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebRootConfig.class})
public class TestBuildXml {

    private User user;

    private List<Step> steps;

    private List<Loop> loops;

    @Autowired
    private CoreAcctRecordConverter coreAcctRecordConverterImpl;

    @Autowired
    private CoreAcctRecordConverter coreAcctRecordConverterImplNew;

    @Before
    public void before(){
        user=new User();
        user.setUserName("张三");
        user.setPassWord("Q1876310z");
        steps=new ArrayList<>(10);
        loops=new ArrayList<>(5);
        for (int i=0;i<10;i++){
            Step step=new Step();
            step.setStepName("step_"+i);
            step.setStepIndex(i);
            steps.add(step);
        }

        for (int i = 0; i < 5; i++) {
            Loop loop=new Loop();
            loop.setSteps(steps);
            loops.add(loop);
        }
    }

//    @Test
//    public void testBuildXml(){
//        System.out.println(coreAcctRecordConverterImpl.encode(user,steps));
//    }




    @Test
    public void testBuildXmlNew(){
        System.out.println(coreAcctRecordConverterImplNew.encode(user,steps));
    }




    @Test
    public void testSpel(){
        ExpressionParser parser=new SpelExpressionParser();
        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("user",user);
        context.setVariable("a",1);
        context.setVariable("b",2);
        String spel="#user.getUserName()";
        String value=parser.parseExpression(spel).getValue(context,String.class);
        System.out.println(value);
        String caSple="#a+#b";
        System.out.println(parser.parseExpression(caSple).getValue(context,String.class));
    }

    @Test
    public void testSteps(){
        ExpressionParser parser=new SpelExpressionParser();
        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("steps",steps);
        String spel="#steps.get(#i).getStepName()";
        for (int i = 0; i < steps.size(); i++) {
            context.setVariable("i",i);
            System.out.println(parser.parseExpression(spel).getValue(context,String.class));
        }
    }

    @Test
    public void testLoops(){
        ExpressionParser parser=new SpelExpressionParser();
        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("loops",loops);
        String spel="#loops.get(#i).getSteps().get(#j).getStepName()";
        for (int i = 0; i < loops.size(); i++) {
            context.setVariable("i",i);
            List<Step> tempSteps = loops.get(i).getSteps();
            for (int j = 0; j < tempSteps.size(); j++) {
                context.setVariable("j",j);
                System.out.println(parser.parseExpression(spel).getValue(context,String.class));
            }
            System.out.println("-------------");
        }
    }

    @Test
    public void testData(){
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2=new SimpleDateFormat("HH:mm:ss");
        System.out.println(format1.format(time));
        System.out.println(format2.format(time));
    }
}
