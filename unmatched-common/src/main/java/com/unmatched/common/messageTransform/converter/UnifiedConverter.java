package com.unmatched.common.messageTransform.converter;

import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.face.MessageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
* Description: 统一的报文转换器
* @author: yuhang tao
* @date: 2020/3/13
* @version: v1.0
*/
@Component
public class UnifiedConverter {

    @Autowired
    private MessageOperation messageOperation;

    //现在只是返回xml报文，等代码迁移到xir会改为返回List<ExecuteContext>
    public String encode(OperationType type,EvaluationContext context){
        return messageOperation.getMessage(type,context);
    }
}
