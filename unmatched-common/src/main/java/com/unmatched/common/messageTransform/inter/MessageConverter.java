package com.unmatched.common.messageTransform.inter;

import com.unmatched.common.messageTransform.entity.MessageNodeInfo;

import java.util.List;

/**
* Description: 任何报文转换器都要实现该接口
 * 包括XML、JSON
* @author: yuhang tao
* @date: 2019/12/23
* @version: v1.0
*/
public interface MessageConverter {
    /**
    * Description: 构建报文
    * @date: 2019/12/24
    */
    String convertMessage(MessageNodeInfo messageNodeInfo);
}
