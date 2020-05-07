package com.unmatched;

import com.google.gson.Gson;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSPEL {

    private String condition1="#i<#steps.size()";

    private String condition2="#i++";

    private List<Step> steps;

    private LinkedList<Step> list=new LinkedList<>();

    private ExpressionParser parser=new SpelExpressionParser();

    private String jsonStr="12312[111[qwewqw][zzzzz]000][222]";

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

    @Test
    public void testPattern1(){
        String str="<username>$#user.getUserName()$</username><username>$#user.getUserName()$</username>";
        Pattern pattern=Pattern.compile("\\$.+?\\$");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }

    @Test
    public void testPattern2(){
        String str="<username>$#user.getUserName()$</username><username>$#user.getPassWord()$</username>";
        //这里加一个？表示使用勉强型而不是贪婪型
        Pattern pattern=Pattern.compile("\\$.+?\\$");
        Matcher matcher = pattern.matcher(str);
        EvaluationContext context=new StandardEvaluationContext();
        ExpressionParser parser=new SpelExpressionParser();
        User user=new User();
        user.setUserName("wqwq");
        user.setPassWord("1231");
        context.setVariable("user",user);
        StringBuffer buffer=new StringBuffer();
        while (matcher.find()){
            //执行渐进式替换
            matcher.appendReplacement(buffer,
                    parser.parseExpression(matcher.group().
                            replace("$","")).getValue(context,String.class));
        }
        //将输入字符串余下的部分复制到buffer中
        System.out.println(matcher.appendTail(buffer));
    }

    @Test
    public void testLoopPa(){
        String str="<step><name>$#user.getUserName()$</name><passWord>$#user.getPassWord()$</passWord></step>";
        System.out.println(OperationType.CORE_ACCT_RECORD.name());
    }

    private void findLoopNodeAndConditions(String template){
        Pattern pattern = Pattern.compile("(\\*\\*\\$(.+?)\\$)(<.+?>)");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()){
            String condition=matcher.group(2);
            String loopNode=matcher.group(3);
            String replace = StringUtils.replace(loopNode, "<", "</");
            Pattern contentPa=Pattern.compile(loopNode+".*"+replace);
        }
    }

    @Test
    public void testMap(){
        Map<Integer,String> tmp=new HashMap<>();
        tmp.put(1,"1");
        tmp.put(2,"2");
        tmp.put(3,"3");
        Set<Map.Entry<Integer, String>> entries = tmp.entrySet();
        Iterator<Map.Entry<Integer, String>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, String> next = iterator.next();
            System.out.println(next.getKey());
            iterator.remove();
            Set<Map.Entry<Integer, String>> entries1 = tmp.entrySet();
            for (Map.Entry<Integer, String> entry : entries1) {
                System.out.println(entry.getKey());
            }
        }
        System.out.println(tmp.size());
    }

    @Test
    public void testChar(){
        System.out.println((char) -1);
    }

    @Test
    public void testFindJsonArr(){
        char arrTag='[';
        char rightTag=']';
        Stack<String> stack=new Stack<>();
        for (int i = 0; i < jsonStr.length(); i++) {
            if (jsonStr.charAt(i)==rightTag){
                if(!stack.empty())
                    System.out.println(stack.pop());
            }
            for (int i1 = 0; i1 < stack.size(); i1++) {
                stack.set(i1,stack.get(i1)+jsonStr.charAt(i));
            }
            if (jsonStr.charAt(i)==arrTag){
                String tmp="";
                stack.push(tmp);
            }
        }
    }

    @Test
    public void testRegx(){
        String str="123{12}12{12}";
        Pattern pattern=Pattern.compile(getStr());
        pattern.matcher(str);
//        System.out.println(s);
    }

    private String getStr(){
        return "{" +
                "\"flowId\": \"@#entries.get(#i).getFlowId()@\"," +
                "\"value\": \"@#entries.get(#i).getValue()@\"," +
                "\"cdFlag\": \"@#entries.get(#i).getDebitCreditFlag()=='c'?0:1@\"" +
                "}";
    }
}
