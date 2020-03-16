package com.unmatched.common.messageTransform.entity;

import lombok.Data;

/**
* Description: 初始化实体类
* @author: yuhang tao
* @date: 2020/3/13
* @version: v1.0
*/
@Data
public class InitialEntity {

    //参数名
    private String paramName;

    //初始化值
    private String initValue;

    //参数类型
    private String classes;
}
