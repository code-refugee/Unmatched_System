package com.unmatched.common.messageTransform.impl;

import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.common.messageTransform.exception.MessageConverterException;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Description: XML报文转换器
 *
 * @author: yuhang tao
 * @date: 2019/12/24
 * @version: v1.0
 */
@Component
public class SimpleXmlMessageConverterIml implements MessageConverter {

    @Override
    public String convertMessage(MessageNodeInfo messageNodeInfo) {
        if (messageNodeInfo instanceof XMLMessageNodeInfo) {
            XMLMessageNodeInfo xMLMessageNodeInfo = (XMLMessageNodeInfo) messageNodeInfo;
            String template = xMLMessageNodeInfo.getName() + "\n";
            List<XMLMessageNodeInfo> childs = xMLMessageNodeInfo.getChilds();
            for (XMLMessageNodeInfo info : childs) {
                template = convertMessage(info, template, 0);
            }
            return template;
        } else {
            throw new MessageConverterException("所传入的节点信息类不是XML节点信息类" +
                    messageNodeInfo.getClass().getSimpleName());
        }
    }

    private String convertMessage(XMLMessageNodeInfo XMLMessageNodeInfo, String template, int index) {

        if (XMLMessageNodeInfo == null)
            return template;

        String temp = "";

        for (int i = 0; i < index; i++) {
            //增加缩进
            temp += "\t";
        }

        template += temp + "<" + XMLMessageNodeInfo.getName() + ">" + "\n";
        if (XMLMessageNodeInfo.getValue() != null) {
            String temp2 = "\t" + temp;
            template += temp2 + XMLMessageNodeInfo.getValue() + "\n";
        }
        if (XMLMessageNodeInfo.getChilds() == null) {
            template += temp + "</" + XMLMessageNodeInfo.getName() + ">" + "\n";
            return template;
        }
        int k = index;
        k++;
        List<XMLMessageNodeInfo> childs = XMLMessageNodeInfo.getChilds();
        for (int i = 0; i < childs.size(); i++) {
            template = convertMessage(childs.get(i), template, k);
        }
        template += temp + "</" + XMLMessageNodeInfo.getName() + ">" + "\n";
        return template;
    }
}
