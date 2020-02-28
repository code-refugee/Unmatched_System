package com.unmatched.controller;

import com.unmatched.common.exception.BaseRootException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* Description: 统一的Controller，用于统一处理异常等
 * @ControllerAdvicee注解本身已经使用了@Component，因此@ControllerAdvice注
 * 解所标注的类将会自动被组件扫描获取到
* @author: yuhang tao
* @date: 2019/12/14
* @version: v1.0
*/
@ControllerAdvice
public class UnifiedController {
    @ExceptionHandler(BaseRootException.class)
    @ResponseBody
    public String handleBaseRootException(Exception ex){
        return "发生异常"+ex+"，即将跳转至登录页面";
    }
}
