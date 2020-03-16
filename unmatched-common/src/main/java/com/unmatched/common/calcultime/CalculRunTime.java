package com.unmatched.common.calcultime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CalculRunTime {
    //时间单位 只支持 毫秒（ms）、纳秒（ns）
    String unit() default"ms";
}
