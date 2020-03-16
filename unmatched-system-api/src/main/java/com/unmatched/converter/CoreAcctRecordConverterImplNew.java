package com.unmatched.converter;

import com.unmatched.common.messageTransform.MessageOperationOld;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CoreAcctRecordConverterImplNew implements CoreAcctRecordConverter {

    @Autowired
    private MessageOperationOld messageOperationOld;

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public String encode(User user, List<Step> steps) {
        //获取与记账有关的节点
        XMLMessageNodeInfo rootNodeInfo = messageOperationOld.getAllXMLNodeInfo(OperationType.CORE_ACCT_RECORD);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", user);
        context.setVariable("steps", steps);

        //临时节点，用于保存原来节点的状态
        XMLMessageNodeInfo temp1 = new XMLMessageNodeInfo();
        //浅拷贝
        BeanUtils.copyProperties(messageOperationOld.getXmlNodeInfoById("05"), temp1);
        List<XMLMessageNodeInfo> tempList = new ArrayList<>();
        for (int i = 1; i < steps.size(); i++) {
            EvaluationContext tempContext = new StandardEvaluationContext();
            tempContext.setVariable("i", i);
            tempContext.setVariable("steps", steps);
            XMLMessageNodeInfo temp2;
            //浅拷贝
            BeanUtils.copyProperties(messageOperationOld.getXmlNodeInfoById("05"), temp1);
            messageOperationOld.putValueToNode(tempContext, temp1);
            //深拷贝
            temp2 = messageOperationOld.copy(temp1);
            tempList.add(temp2);
        }
        XMLMessageNodeInfo needAddChildsNode = messageOperationOld.getXmlNodeInfoById(messageOperationOld.getXmlNodeInfoById("05").getParentId());
        if (null != needAddChildsNode) {
            if (needAddChildsNode.getChilds() != null) {
                needAddChildsNode.getChilds().addAll(tempList);
            }
        }
        context.setVariable("i", 0);
        messageOperationOld.putValueToTemplate(context);
        return messageOperationOld.getMessage(rootNodeInfo, messageConverter);
    }

}
