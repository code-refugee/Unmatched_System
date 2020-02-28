package com.unmatched.common.messageTransform.impl;

import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
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
        String template = messageNodeInfo.getName() + "\n";
        List<MessageNodeInfo> childs = messageNodeInfo.getChilds();
        for (MessageNodeInfo info : childs) {
            template = convertMessage(info, template, 0);
        }
        return template;
    }

    private String convertMessage(MessageNodeInfo messageNodeInfo, String template, int index) {

        if (messageNodeInfo == null)
            return template;

        String temp = "";

        for (int i = 0; i < index; i++) {
            //增加缩进
            temp += "\t";
        }

        template += temp + "<" + messageNodeInfo.getName() + ">" + "\n";
        if (messageNodeInfo.getValue() != null) {
            String temp2 = "\t" + temp;
            template += temp2 + messageNodeInfo.getValue() + "\n";
        }
        if (messageNodeInfo.getChilds() == null) {
            template += temp + "</" + messageNodeInfo.getName() + ">" + "\n";
            return template;
        }
        int k = index;
        k++;
        List<MessageNodeInfo> childs = messageNodeInfo.getChilds();
        for (int i = 0; i < childs.size(); i++) {
            template = convertMessage(childs.get(i), template, k);
        }
        template += temp + "</" + messageNodeInfo.getName() + ">" + "\n";
        return template;
    }
}
