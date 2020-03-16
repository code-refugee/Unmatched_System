package com.unmatched.common.messageTransform.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
* Description: 用于存放XML报文节点信息
 * 可以考虑增加一个Map用于存放节点的属性和值
* @author: yuhang tao
* @date: 2019/12/24
* @version: v1.0
*/
@Data
@ToString
public class XMLMessageNodeInfo implements MessageNodeInfo{

    //节点Id
    private String id;

    //父节点Id
    private String parentId;

    //节点名
    private String name;

    //节点值
    private String value;

    //值来源(SPEL表达式)
    private String valueFrom;

    //是否需要循环
    private String ifNeedLoop;

    //循环条件
    private String conditions;

    //子节点
    private List<XMLMessageNodeInfo> childs;


}
