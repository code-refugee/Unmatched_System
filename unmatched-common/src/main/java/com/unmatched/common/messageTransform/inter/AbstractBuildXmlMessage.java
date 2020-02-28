//package com.unmatched.common.messageTransform.inter;
//
//import com.unmatched.common.messageTransform.datastructure.MultiTree;
//import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Description: 抽象XML报文构建器，构建任何xml报文都要继承此类
// *
// * @author: yuhang tao
// * @date: 2019/12/24
// * @version: v1.0
// */
//@Data
//public abstract class AbstractBuildXmlMessage {
//
//    //自定义xml编码
//    private String encoding;
//
//    //自定义xml版本
//    private String version;
//
//    //自定义xml申明
//    private String xmlDeclare;
//
//    //节点路径分隔符
//    public static final String SEPARATOR_CHARS = "/";
//
//    //默认编码方式
//    private static final String DEFAULT_ENCODING = "UTF-8";
//
//    //默认xml版本
//    private static final String DEFAULT_VERSION = "1.0";
//
//    //xml申明，定义xml的版本和所使用的编码
//    private static final String DEFAULT_XML_DECLARE = "<?xml version='1.0' encoding='UTF-8'?>";
//
//    //先构建XML Template 再去赋值
////    public String buildXML(List<MessageNodeInfo> originNodeInfos,List<MessageNodeInfo> messageNodeInfos){
////        StringBuilder stringBuilder=new StringBuilder(getFinalDeclare());
////        Map<String, List<MessageNodeInfo>> groupedOriginNodeInfos = groupByParentNode(originNodeInfos);
////        Map<String, List<MessageNodeInfo>> groupedMessageNodeInfos = groupByParentNode(messageNodeInfos);
////        for (Map.Entry<String,List<MessageNodeInfo>> entry:groupedOriginNodeInfos.entrySet()){
////            MultiTree multiTree=new MultiTree();
////            //先根据groupedOriginNodeInfos去构建模板
////            for(MessageNodeInfo nodeInfo:entry.getValue()){
////                multiTree.put(nodeInfo.getName(),nodeInfo.getPath());
////            }
////            //模板构建好之后再去为节点赋值
////            List<MessageNodeInfo> infos = groupedMessageNodeInfos.get(entry.getKey());
////            //根据节点路径的长短不同进行排序
////            Collections.sort(infos);
////            while (infos.size()>0){
////                List<Integer> deleteAfterPutIndex=new ArrayList<>(infos.size());
////            }
////        }
////    }
//
//    private void putValueToMultiTree(MessageNodeInfo info, List<MessageNodeInfo> infos, List<Integer> deleteAfterPutIndex,
//                                     MultiTree multiTree, int index){
//        deleteAfterPutIndex.add(index);
//        for (int i=index+1;i<infos.size();i++){
//
//        }
//    }
//
//    //根据父节点的不同进行分组
//    private Map<String, List<MessageNodeInfo>> groupByParentNode(List<MessageNodeInfo> messageNodeInfos) {
//        Map<String, List<MessageNodeInfo>> groupMap = new HashMap<>();
//        for (MessageNodeInfo info : messageNodeInfos) {
//            String[] split = StringUtils.split(info.getPath(), SEPARATOR_CHARS);
//            if (null != split && 0 < split.length) {
//                if (groupMap.containsKey(split[0])) {
//                    groupMap.get(split[0]).add(info);
//                } else {
//                    List<MessageNodeInfo> infos = new ArrayList<>();
//                    infos.add(info);
//                    groupMap.put(split[0], infos);
//                }
//            }
//        }
//        return groupMap;
//    }
//
//    //获取最终的XML申明
//    private String getFinalDeclare() {
//        //如果有自定义的申明就以自定义的为主
//        if (!StringUtils.isBlank(xmlDeclare))
//            return xmlDeclare;
//
//        String tempXmlDeclare = DEFAULT_XML_DECLARE;
//        if (!StringUtils.isBlank(version)) {
//            tempXmlDeclare = StringUtils.replace(tempXmlDeclare, DEFAULT_VERSION, version);
//        }
//        if (!StringUtils.isBlank(encoding)) {
//            tempXmlDeclare = StringUtils.replace(tempXmlDeclare, DEFAULT_ENCODING, encoding);
//        }
//        return tempXmlDeclare;
//    }
//
//}
