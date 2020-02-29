package com.unmatched.common.messageTransform;

import com.google.gson.Gson;
import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.exception.MessageConverterException;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import com.unmatched.common.messageTransform.mapper.MessageNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageOperation {

    @Autowired
    private MessageNodeMapper messageNodeMapper;

    //缓存
    private Map<String, XMLMessageNodeInfo> cache = new HashMap<>();

    private Gson gson = new Gson();

    /**
    * Description: 调用此方法可以将传入的类转换成xml或json消息
     * @param nodeInfo ： 需要转换的对象
     * @param transform : 消息转换器
    * @date: 2020/2/29
    */
    public String getMessage(MessageNodeInfo nodeInfo, MessageConverter transform) {
        if (nodeInfo == null || transform == null)
            throw new IllegalArgumentException("传入参数不能为null");
        return transform.convertMessage(nodeInfo);
    }

    /**
    * Description: 根据类型得到所有的节点信息 （节点信息不会经常变动，可以考虑加入缓存）
     * @param type ： 如 记账、支付、抹账
     * @return : 返回的对象 其实是一个抽象的xml模板
    * @date: 2020/2/29
    */
    public XMLMessageNodeInfo getAllXMLNodeInfo(OperationType type) {
        List<XMLMessageNodeInfo> nodeInfos = messageNodeMapper.findXMLNodeByUseFor(type.getUseFor());
        return handleXMLNodeInfos(nodeInfos);
    }

    public XMLMessageNodeInfo getXmlNodeInfoById(String id) {
        return cache.get(id);
    }

    //BeanUtils.copyProperties是浅拷贝，这里我们需要深拷贝
    public XMLMessageNodeInfo copyProperties(XMLMessageNodeInfo source) {

        if (null == source)
            return null;

        String json = gson.toJson(source);
        return gson.fromJson(json, XMLMessageNodeInfo.class);
    }

    /**
    * Description: 将得到的所有节点拼成一个xml模板
    * @date: 2020/2/29
    */
    private XMLMessageNodeInfo handleXMLNodeInfos(List<XMLMessageNodeInfo> nodeInfos) {
        XMLMessageNodeInfo root = getRootMessageNode(nodeInfos);
        if (null != root) {
            addChildMessageNodes(root, nodeInfos);
            return root;
        } else {
            throw  new MessageConverterException("未在数据库中定义根节点");
        }
    }

    //获取根节点
    private XMLMessageNodeInfo getRootMessageNode(List<XMLMessageNodeInfo> nodeInfos) {
        if(null==nodeInfos)
            return null;

        XMLMessageNodeInfo root = null;
        int countRoot=0;
        //根节点没有父节点且唯一
        for (int i = 0; i < nodeInfos.size(); i++) {
            if (null == nodeInfos.get(i).getParentId() ||
                    "".equals(nodeInfos.get(i).getParentId())) {
                root = nodeInfos.get(i);
                nodeInfos.remove(i);
                cache.put(root.getId(), root);
                countRoot++;
            }
        }

        if(countRoot>1)
            throw new MessageConverterException("数据库中存在多个根节点");

        return root;
    }

    //添加子节点(这一步比较耗时，得优化)
    private void addChildMessageNodes(XMLMessageNodeInfo nodeInfo, List<XMLMessageNodeInfo> nodeInfos) {
        Iterator<XMLMessageNodeInfo> iterator = nodeInfos.iterator();
        while (iterator.hasNext()) {
            XMLMessageNodeInfo temp = iterator.next();
            if (temp.getParentId().equals(nodeInfo.getId())) {
                if (nodeInfo.getChilds() != null) {
                    nodeInfo.getChilds().add(temp);
                } else {
                    List<XMLMessageNodeInfo> childs = new ArrayList<>();
                    childs.add(temp);
                    nodeInfo.setChilds(childs);
                }
                cache.put(temp.getId(), temp);
                //将添加过的节点移除，减少不必要的循环
//                iterator.remove();
                //递归，添加该节点的子节点
                addChildMessageNodes(temp, nodeInfos);
            }
        }
    }

}
