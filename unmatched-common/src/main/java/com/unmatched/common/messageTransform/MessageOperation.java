package com.unmatched.common.messageTransform;

import com.google.gson.Gson;
import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import com.unmatched.common.messageTransform.mapper.MessageNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageOperation {

    @Autowired
    private MessageNodeMapper messageNodeMapper;

    private Map<String,MessageNodeInfo> cache=new HashMap<>();

    private Gson gson=new Gson();

    public String getMessage(MessageNodeInfo nodeInfo, MessageConverter transform) {
        return transform.convertMessage(nodeInfo);
    }

    public MessageNodeInfo getNodeInfo(OperationType type) {
        List<MessageNodeInfo> nodeInfos = messageNodeMapper.findByUseFor(type.getUseFor());
        return handleNodeInfos(nodeInfos);
    }

    public MessageNodeInfo getNodeInfoById(String id){
        return cache.get(id);
    }

    //BeanUtils.copyProperties是浅拷贝，这里我们需要深拷贝
    public MessageNodeInfo copyProperties(MessageNodeInfo source){
        String json = gson.toJson(source);
        return gson.fromJson(json,MessageNodeInfo.class);
    }

    private MessageNodeInfo handleNodeInfos(List<MessageNodeInfo> nodeInfos) {
        MessageNodeInfo root = getRootMessageNode(nodeInfos);
        if (null != root) {
            addChildMessageNodes(root, nodeInfos);
            return root;
        } else {
            //应该抛出异常
            return null;
        }
    }

    //获取根节点
    private MessageNodeInfo getRootMessageNode(List<MessageNodeInfo> nodeInfos) {
        checkList(nodeInfos);
        MessageNodeInfo root = null;
        //根节点没有父节点且唯一（这里没有检查唯一性，后面补上）
        for (int i = 0; i < nodeInfos.size(); i++) {
            if (null == nodeInfos.get(i).getParentId() ||
                    "".equals(nodeInfos.get(i).getParentId())) {
                root = nodeInfos.get(i);
                nodeInfos.remove(i);
                cache.put(root.getId(),root);
                return root;
            }
        }
        return root;
    }

    //获取子节点
    private void addChildMessageNodes(MessageNodeInfo nodeInfo, List<MessageNodeInfo> nodeInfos) {
        Iterator<MessageNodeInfo> iterator = nodeInfos.iterator();
        while (iterator.hasNext()) {
            MessageNodeInfo temp = iterator.next();
            if (temp.getParentId().equals(nodeInfo.getId())) {
                if (nodeInfo.getChilds() != null) {
                    nodeInfo.getChilds().add(temp);
                } else {
                    List<MessageNodeInfo> childs = new ArrayList<>();
                    childs.add(temp);
                    nodeInfo.setChilds(childs);
                }
                cache.put(temp.getId(),temp);
                //将添加过的节点移除，减少不必要的循环
//                iterator.remove();
                //递归，添加该节点的子节点
                addChildMessageNodes(temp, nodeInfos);
            }
        }
    }

    public void checkList(List<MessageNodeInfo> nodeInfos) {
        if (null == nodeInfos)
            throw new IllegalArgumentException("无可用的报文节点!");
    }
}
