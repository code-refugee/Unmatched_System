package com.unmatched;

import com.google.gson.Gson;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.pojo.Step;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TestSPEL {

    private String condition1="#i<#steps.size()";

    private String condition2="#i++";

    private List<Step> steps;

    private LinkedList<Step> list=new LinkedList<>();

    @Before
    public void before(){
        steps=new ArrayList<>(10);
        for (int i=0;i<10;i++){
            Step step=new Step();
            step.setStepName("step_"+i);
            step.setStepIndex(i);
            steps.add(step);
            list.addFirst(step);
        }
    }

    @Test
    public void testLink(){
        while (!list.isEmpty()){
            System.out.println(list.removeFirst());
        }
    }

    @Test
    public void testLoop(){
        ExpressionParser parser=new SpelExpressionParser();
        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("i",0);
        context.setVariable("steps",steps);
        EvaluationContext copyd=new StandardEvaluationContext();
        try {
            BeanUtils.copyProperties(copyd,context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int k=0;
        while (parser.parseExpression(condition1).getValue(context,Boolean.class)){
//            parser.parseExpression(condition2).setValue();
            System.out.println("这是第"+k+"次循环");
            parser.parseExpression(condition2).getValue(context,String.class);
            k++;
        }

        context.setVariable("i",0);

    }

    public EvaluationContext copy(EvaluationContext source) {

        if (null == source)
            return null;

        Gson gson=new Gson();

        String json = gson.toJson(source);
        return gson.fromJson(json, StandardEvaluationContext.class);
    }
}
