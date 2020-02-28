package com.unmatched.converter;

import com.unmatched.common.messageTransform.MessageOperation;
import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import com.unmatched.pojo.Step;
import com.unmatched.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CoreAcctRecordConverterImpl implements CoreAcctRecordConverter {

    @Autowired
    private MessageOperation messageOperation;

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public String encode(User user, List<Step> steps) {
        MessageNodeInfo rootNodeInfo = messageOperation.getNodeInfo(OperationType.CORE_ACCT_RECORD);
        //username
        MessageNodeInfo nodeInfo02 = messageOperation.getNodeInfoById("02");
        //password
        MessageNodeInfo nodeInfo03 = messageOperation.getNodeInfoById("03");
        if(nodeInfo02!=null)
            nodeInfo02.setValue(user.getUsername());
        if(nodeInfo03!=null)
            nodeInfo03.setValue(user.getPassword());
        int count=1;
        MessageNodeInfo temp1=new MessageNodeInfo();
        //浅拷贝
        BeanUtils.copyProperties(messageOperation.getNodeInfoById("05"),temp1);
        List<MessageNodeInfo> tempList=new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            MessageNodeInfo temp2=new MessageNodeInfo();
            //浅拷贝
            BeanUtils.copyProperties(temp1,messageOperation.getNodeInfoById("05"));
            if(i!=steps.size()-1){
                //stepName
                MessageNodeInfo nodeInfo06=messageOperation.getNodeInfoById("06");
                //stepIndex
                MessageNodeInfo nodeInfo07=messageOperation.getNodeInfoById("07");
                if (nodeInfo06!=null)
                    nodeInfo06.setValue(steps.get(i).getStepName());
                if(nodeInfo07!=null)
                    nodeInfo07.setValue(steps.get(i).getStepIndex()+"");
                //深拷贝
                temp2=messageOperation.copyProperties(messageOperation.getNodeInfoById("05"));
                tempList.add(temp2);
            }else {
                //stepName
                MessageNodeInfo nodeInfo06=messageOperation.getNodeInfoById("06");
                //stepIndex
                MessageNodeInfo nodeInfo07=messageOperation.getNodeInfoById("07");
                if (nodeInfo06!=null)
                    nodeInfo06.setValue(steps.get(i).getStepName());
                if(nodeInfo07!=null)
                    nodeInfo07.setValue(steps.get(i).getStepIndex()+"");
            }
        }
        MessageNodeInfo needAddChildsNode=messageOperation.getNodeInfoById(messageOperation.getNodeInfoById("05").getParentId());
        List<MessageNodeInfo> childs = needAddChildsNode.getChilds();
        childs.addAll(tempList);
        return messageOperation.getMessage(rootNodeInfo,messageConverter);
    }
}
