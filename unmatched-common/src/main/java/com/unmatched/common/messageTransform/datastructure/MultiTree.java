package com.unmatched.common.messageTransform.datastructure;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Description:多叉树，用于构造xml报文模板
 *
 * @author: yuhang tao
 * @date: 2019/12/26
 * @version: v1.0
 */
public class MultiTree {

    private Node root;

    //节点路径分隔符
    public static final String SEPARATOR_CHARS = "/";

    public Set<Node> recordHasPuted;

    private class Node {
        //节点名
        private String name;
        //节点值
        public String value;
        //节点路径
        public String path;
        //该节点的子节点
        private List<Node> childs;

        public Node(String name, String path) {
            this.name = name;
            this.path = path;
            this.childs = null;
            this.value = null;
        }

        //重写equals和hashCode方法，放到Set集中会调用这两个方法
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name) && path.equals(node.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name) + Objects.hash(path);
        }
    }

    public MultiTree() {
        this.root = null;
        this.recordHasPuted = new HashSet<>();
    }

    public boolean hasChild(Node node) {
        return CollectionUtils.isNotEmpty(node.childs);
    }

    public Node getRoot() {
        return root;
    }

    public String getNodeName(Node node) {
        if (null == node)
            throw new IllegalArgumentException("节点不能为null");
        return node.name;
    }

    public String getNodePath(Node node) {
        if (null == node)
            throw new IllegalArgumentException("节点不能为null");
        return node.path;
    }

    public List<Node> getChilds(Node node) {
        if (null == node)
            throw new IllegalArgumentException("节点不能为null");
        return node.childs;
    }

    public void put(String nodeName, String path) {

        if (StringUtils.isBlank(nodeName) && StringUtils.isBlank(path))
            throw new IllegalArgumentException("节点名和节点路径不能为空！");

        String[] parentNodename = StringUtils.split(path, SEPARATOR_CHARS);

        if (root == null) {
            root = new Node(parentNodename[0], null);
        }

        Node temp = root;

        if (!parentNodename[0].equals(getNodeName(temp)))
            throw new IllegalArgumentException("未能匹配到根节点" + parentNodename[0]);

        String tempPath = parentNodename[0] + SEPARATOR_CHARS;
        for (int i = 1; i < parentNodename.length; i++) {
            temp = put(temp, parentNodename[i], tempPath);
            tempPath += parentNodename[i] + SEPARATOR_CHARS;
        }

        put(temp, nodeName, path);
    }

//    public void put(String nodeName,String path,String nodeValue){
//
//        if (StringUtils.isBlank(nodeName) && StringUtils.isBlank(path))
//            throw new IllegalArgumentException("节点名和节点路径不能为空！");
//
//        String[] parentNodename = StringUtils.split(path, SEPARATOR_CHARS);
//
//        if(parentNodename.length==1){
//            List<Node> childs = getChilds(root);
//            for (Node node:childs){
//                if(getNodeName(node).equals(nodeName)){
//                    if(StringUtils.isNotEmpty(node.value)){
//                        node.value= nodeValue;
//                        return;
//                    }
//                }
//            }
//            Node node=new Node(nodeName,nodeValue);
//            childs.add(node);
//        }else {
//            Node grandpaNodeName;
//            Node fatherNodeName;
//            for ()
//        }
//    }

    //根据路径获取节点
    public Node getNode(String path) {
        return getNode(root, path);
    }

    public Node getNode(Node node, String path) {

        if (node == null || StringUtils.isBlank(path))
            throw new IllegalArgumentException("节点或节点路径不能为空！");

        String[] parentNodename = StringUtils.split(path, SEPARATOR_CHARS);

        Node temp = node;

        boolean hasNode = true;

        for (int i = 1; i < parentNodename.length; i++) {
            List<Node> childs = getChilds(temp);
            if (CollectionUtils.isNotEmpty(childs)) {
                for (Node node1 : childs) {
                    if (getNodeName(node1).equals(parentNodename[i])) {
                        temp = node1;
                        hasNode = true;
                        break;
                    }
                    hasNode = false;
                }
            } else {
                hasNode = false;
            }

            if (!hasNode)
                throw new IllegalArgumentException("节点" + getNodeName(node) + "下找不到路径为" + path + "的节点");
        }


        return temp;
    }

    public void add(Node parent,Node node){

    }

    public String getTemplate() {
        return getTemplate(root, "", 0);
    }

    /**
     * Description: 将多叉树的所有节点取出，拼报文
     *
     * @param node     :节点
     * @param template : xml模板
     * @param indent   : 产生缩进的个数
     * @date: 2019/12/26
     */
    private String getTemplate(Node node, String template, int indent) {
        if (node == null)
            return template;

        String temp = "";

        for (int i = 0; i < indent; i++) {
            //增加缩进
            temp += "\t";
        }

        template += temp + "<" + getNodeName(node) + ">" + "\n";

        if (!hasChild(node)) {
            template += temp + "</" + getNodeName(node) + ">" + "\n";
            return template;
        }

        int k = indent;
        k++;

        List<Node> childs = getChilds(node);

        for (int i = 0; i < childs.size(); i++) {
            //递归调用
            template = getTemplate(childs.get(i), template, k);
        }
        template += temp + "</" + getNodeName(node) + ">" + "\n";

        return template;
    }

    private Node put(Node node, String childName, String path) {
        List<Node> childs = getChilds(node);
        if (childs == null) {
            childs = new ArrayList<>(10);
            node.childs = childs;
        }
        Node temp = checkExist(getChilds(node), childName, path);
        if (null == temp) {
            temp = new Node(childName, path);
            getChilds(node).add(temp);
        }
        return temp;
    }

    private Node checkExist(List<Node> nodes, String nodeName, String nodePath) {

        if (nodes.size() == 0)
            return null;

        for (int i = 0; i < nodes.size(); i++) {
            if (nodeName.equals(getNodeName(nodes.get(i))) && nodePath.equals(getNodePath(nodes.get(i)))) {
                return nodes.get(i);
            }
        }

        return null;
    }
}
