package com.unmatched.common.messageTransform.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
* Description: 用于存放报文节点信息
* @author: yuhang tao
* @date: 2019/12/24
* @version: v1.0
*/
@Data
@ToString
public class MessageNodeInfo{

    //节点标识
    private String id;

    //父节点
    private String parentId;

    //节点名
    private String name;

    //节点值
    private String value;

    private List<MessageNodeInfo> childs;


}
