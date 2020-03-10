package com.unmatched.converter;

import com.unmatched.common.messageTransform.MessageOperation;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
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
        XMLMessageNodeInfo rootNodeInfo = messageOperation.getAllXMLNodeInfo(OperationType.CORE_ACCT_RECORD);
        //username
        XMLMessageNodeInfo nodeInfo02 = messageOperation.getXmlNodeInfoById("02");
        //password
        XMLMessageNodeInfo nodeInfo03 = messageOperation.getXmlNodeInfoById("03");
        //如果得到的是null就new一个类而不是null，这样代码就不用判空了
        if(nodeInfo02!=null)
            nodeInfo02.setValue(user.getUserName());
        if(nodeInfo03!=null)
            nodeInfo03.setValue(user.getPassWord());
        int count=1;
        XMLMessageNodeInfo temp1=new XMLMessageNodeInfo();
        //浅拷贝
        BeanUtils.copyProperties(messageOperation.getXmlNodeInfoById("05"),temp1);
        List<XMLMessageNodeInfo> tempList=new ArrayList<>();
        //按照以往的作法，在这里我们是将值装入一个List或者Map
        //这里我直接复制节点，然后将复制的节点复制
        for (int i = 0; i < steps.size(); i++) {
            XMLMessageNodeInfo temp2=new XMLMessageNodeInfo();
            //浅拷贝
            BeanUtils.copyProperties(temp1,messageOperation.getXmlNodeInfoById("05"));
            if(i!=steps.size()-1){
                //stepName
                XMLMessageNodeInfo nodeInfo06=messageOperation.getXmlNodeInfoById("06");
                //stepIndex
                XMLMessageNodeInfo nodeInfo07=messageOperation.getXmlNodeInfoById("07");
                if (nodeInfo06!=null)
                    nodeInfo06.setValue(steps.get(i).getStepName());
                if(nodeInfo07!=null)
                    nodeInfo07.setValue(steps.get(i).getStepIndex()+"");
                //深拷贝
                temp2=messageOperation.copyProperties(messageOperation.getXmlNodeInfoById("05"));
                tempList.add(temp2);
            }else {
                //stepName
                XMLMessageNodeInfo nodeInfo06=messageOperation.getXmlNodeInfoById("06");
                //stepIndex
                XMLMessageNodeInfo nodeInfo07=messageOperation.getXmlNodeInfoById("07");
                if (nodeInfo06!=null)
                    nodeInfo06.setValue(steps.get(i).getStepName());
                if(nodeInfo07!=null)
                    nodeInfo07.setValue(steps.get(i).getStepIndex()+"");
            }
        }
        XMLMessageNodeInfo needAddChildsNode=messageOperation.getXmlNodeInfoById(messageOperation.getXmlNodeInfoById("05").getParentId());
        List<XMLMessageNodeInfo> childs = needAddChildsNode.getChilds();
        childs.addAll(tempList);
        return messageOperation.getMessage(rootNodeInfo,messageConverter);
    }
}
