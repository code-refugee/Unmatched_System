package com.unmatched.common.calcultime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description: 用于计算被CalculRunTime注解标记的方法的运行时间
 * 注意： 切面只对public作用域的生效
 *
 * @author: yuhang tao
 * @date: 2020/3/13
 * @version: v1.0
 */
@Component
@Aspect
public class CalculRunTimeAspect {

    private long startTime;

    private long endTime;

    @Pointcut("@annotation(calculRunTime)")
    public void pointCut(CalculRunTime calculRunTime) {
    }

    //记录开始时间
    @Before("pointCut(calculRunTime)")
    public void recordStartTime(CalculRunTime calculRunTime) {
        if ("ms".equals(calculRunTime.unit())) {
            startTime = System.currentTimeMillis();
        } else if ("ns".equals(calculRunTime.unit())) {
            startTime = System.nanoTime();
        } else {
            throw new IllegalArgumentException("不支持的计时单位：" + calculRunTime.unit());
        }
    }

    //记录运行时间
    @AfterReturning("pointCut(calculRunTime)")
    public void recordRunTime(JoinPoint joinPoint, CalculRunTime calculRunTime) {
        if ("ms".equals(calculRunTime.unit())) {
            endTime = System.currentTimeMillis();
        } else if ("ns".equals(calculRunTime.unit())) {
            endTime = System.nanoTime();
        } else {
            throw new IllegalArgumentException("不支持的计时单位：" + calculRunTime.unit());
        }
        Signature signature = joinPoint.getSignature();
        System.out.println("调用" + signature.getDeclaringTypeName() +
                "的" + signature.getName() + "方法耗时：" + (endTime - startTime) + calculRunTime.unit());
        System.out.println("<------------------------------------------------------>");
    }
}
