package com.unmatched.pojo;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
* Description:@Data 注解的主要作用是提高代码的简洁，
 * 使用这个注解可以省去代码中大量的get()、 set()、 toString()等方法；
* @author: yuhang tao
* @date: 2019/12/21
* @version: v1.0
*/
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotNull(message = "用户名不得为空！")
    @NotBlank(message = "用户名不得为空！")
    @NotEmpty(message = "用户名不得为空！")
    private String userName;

//    @DecimalMin(value = "18",message = "警告，未满18周岁!")
//    @Pattern(regexp = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\\\W]+$)(?![A-Za-z\\\\W]+$)" +
//            "(?![A-Z0-9\\\\W]+$)[a-zA-Z0-9\\\\W]{8,}$",
//    message = "密码必须包含字母、数字、大小写和特殊符号！")
    private String passWord;

}
