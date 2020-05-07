package com.unmatched.common.messageTransform.face;

import com.google.gson.Gson;
import com.unmatched.common.calcultime.CalculRunTime;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import com.unmatched.common.messageTransform.enums.OperationType;
import com.unmatched.common.messageTransform.exception.MessageConverterException;
import com.unmatched.common.messageTransform.inter.MessageConverter;
import com.unmatched.common.messageTransform.mapper.MessageNodeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("prototype")
public class MessageOperation {

    @Autowired
    private MessageNodeMapper messageNodeMapper;

    @Autowired
    private MessageConverter messageConverter;

    //用作缓存
    private Map<String, XMLMessageNodeInfo> cache;

    //用于决定循环的先后顺序
    private LinkedList<XMLMessageNodeInfo> needLoop;

    private ExpressionParser parser;

    private Gson gson;

    public MessageOperation() {
        cache = new HashMap<>();
        needLoop = new LinkedList<>();
        parser = new SpelExpressionParser();
        gson = new Gson();
    }

    //加上注解用于计算方法执行用时，上生产时最好去掉
    @CalculRunTime
    public String getMessage(OperationType type, EvaluationContext context) {
        XMLMessageNodeInfo allXMLNodeInfo = getAllXMLNodeInfo(type);
        createLoopNode(context);
        putValueToTemplate(context);
        return messageConverter.convertMessage(allXMLNodeInfo);
    }

    /**
     * Description: 根据类型得到所有的节点信息 （节点信息不会经常变动，可以考虑使用@Cacheable加入缓存）
     * 因为该方法是内部调用了，不会走代理，所以@Cacheable已经不起作用了
     *
     * @param type ： 如 记账、支付、抹账
     * @return : 返回的对象 其实是一个抽象的xml模板
     * @date: 2020/2/29
     */
    private XMLMessageNodeInfo getAllXMLNodeInfo(OperationType type) {
        List<XMLMessageNodeInfo> nodeInfos = messageNodeMapper.findXMLNodeByUseFor(type.getUseFor());
        return handleXMLNodeInfos(nodeInfos);
    }

    /**
     * Description: 将得到的所有节点拼成一个xml模板
     *
     * @date: 2020/2/29
     */
    private XMLMessageNodeInfo handleXMLNodeInfos(List<XMLMessageNodeInfo> nodeInfos) {
        XMLMessageNodeInfo root = getRootMessageNode(nodeInfos);
        if (null != root) {
            addChildMessageNodes(root, nodeInfos);
            return root;
        } else {
            throw new MessageConverterException("未在数据库中定义根节点");
        }
    }

    //获取根节点
    private XMLMessageNodeInfo getRootMessageNode(List<XMLMessageNodeInfo> nodeInfos) {
        if (null == nodeInfos)
            return null;

        XMLMessageNodeInfo root = null;
        int countRoot = 0;
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

        if (countRoot > 1)
            throw new MessageConverterException("数据库中存在多个根节点");

        return root;
    }

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
                //将需要循环的节点放入双向队列中
                if (StringUtils.isNotBlank(temp.getIfNeedLoop()) && "1".equals(temp.getIfNeedLoop())) {
                    if (StringUtils.isBlank(temp.getConditions()))
                        throw new MessageConverterException("需要循环的节点未添加循环条件，id：" + temp.getId());
                    String[] conditions = temp.getConditions().split(";");
                    if (conditions.length < 2)
                        throw new MessageConverterException("需要循环的节点缺少循环条件，id：" + temp.getId());
                    needLoop.addFirst(copy(temp));
                }
            }
        }
    }

    //BeanUtils.copyProperties是浅拷贝，这里我们需要深拷贝
    private XMLMessageNodeInfo copy(XMLMessageNodeInfo source) {

        if (null == source)
            return null;

        String json = gson.toJson(source);
        return gson.fromJson(json, XMLMessageNodeInfo.class);
    }

    //为模板赋值
    private void putValueToTemplate(EvaluationContext context) {
        for (Map.Entry<String, XMLMessageNodeInfo> entry : cache.entrySet()) {
            if (!StringUtils.isBlank(entry.getValue().getValueFrom())) {
                entry.getValue().setValue(parser.parseExpression(entry.getValue().
                        getValueFrom()).getValue(context, String.class));
            }
        }
    }

    private void createLoopNode(EvaluationContext context) {
        while (!needLoop.isEmpty()) {
            XMLMessageNodeInfo node = needLoop.removeFirst();
            List<XMLMessageNodeInfo> nodes = new ArrayList<>();
            createLoopNode(context, nodes, node);
            removeChildById(cache.get(node.getParentId()), node.getId());
            cache.get(node.getParentId()).getChilds().addAll(nodes);
            //移除缓存内的该节点，防止再次赋值
            cache.remove(node.getId());
            needLoop.removeFirst();
            //清除双向队列中已经参与过循环的节点
            while (!needLoop.isEmpty() && needLoop.getFirst().getParentId().equals(node.getId())) {
                needLoop.removeFirst();
            }
        }
    }

    private void createLoopNode(EvaluationContext context, List<XMLMessageNodeInfo> nodes, XMLMessageNodeInfo node) {

        boolean hasLoopChild = false;

        if (!needLoop.isEmpty() && needLoop.getFirst().getParentId().equals(node.getId())) {
            removeChildById(node, needLoop.getFirst().getId());
            hasLoopChild = true;
        }

        String[] conditions = node.getConditions().split(";");

        while (parser.parseExpression(conditions[0]).getValue(context, Boolean.class)) {
            XMLMessageNodeInfo temp = copy(node);
            List<XMLMessageNodeInfo> tempNodes = new ArrayList<>();
            if (hasLoopChild) {
                createLoopNode(context, tempNodes, needLoop.removeFirst());
            }
            putValueToNode(context, temp);
            temp.getChilds().addAll(tempNodes);
            //运算其它的表达式并对计数器加一
            for (int i = 1; i < conditions.length; i++) {
                parser.parseExpression(conditions[i]).getValue(context, String.class);
            }
            nodes.add(temp);
        }
        //再次将节点添加回去用于后续循环
        needLoop.addFirst(node);
        //每次循环结束，还原EvaluationContext中的循环计数器
        reactionContext(context, conditions[0]);

    }

    //还原计数器
    private void reactionContext(EvaluationContext context, String condition) {
        int index = condition.indexOf("#");
        String substring = condition.substring(index + 1, index + 2);
        context.setVariable(substring, 0);
    }

    //添加循环的子节点前要先删除原先存在的节点
    private void removeChildById(XMLMessageNodeInfo nodeInfo, String id) {

        if (null == nodeInfo || CollectionUtils.isEmpty(nodeInfo.getChilds()))
            return;

        for (int i = 0; i < nodeInfo.getChilds().size(); i++) {
            if (nodeInfo.getChilds().get(i).getId().equals(id)) {
                nodeInfo.getChilds().remove(i);
                return;
            }
        }
    }

    //为某个节点及其子节点赋值
    private void putValueToNode(EvaluationContext context, XMLMessageNodeInfo nodeInfo) {
        if (nodeInfo == null)
            return;
        if (!StringUtils.isBlank(nodeInfo.getValueFrom())) {
            nodeInfo.setValue(parser.parseExpression(nodeInfo.getValueFrom()).getValue(context, String.class));
        }
        if (CollectionUtils.isEmpty(nodeInfo.getChilds()))
            return;
        for (XMLMessageNodeInfo child : nodeInfo.getChilds()) {
            putValueToNode(context, child);
        }
    }


    //判断一个节点是否为该Id节点的子孙节点
    private boolean ifChildren(String id,XMLMessageNodeInfo nodeInfo){
        if (null==nodeInfo||null==nodeInfo.getParentId()||"".equals(nodeInfo.getParentId()))
            return false;
        if (nodeInfo.getParentId().equals(id))
            return true;
        return ifChildren(id,cache.get(nodeInfo.getParentId()));
    }

}
