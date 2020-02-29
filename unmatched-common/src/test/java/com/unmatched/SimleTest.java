package com.unmatched;

import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import org.junit.Test;

public class SimleTest {
    @Test
    public void simpleTest(){
        MessageNodeInfo messageNodeInfo=new XMLMessageNodeInfo();
        System.out.println(messageNodeInfo.getClass().getSimpleName());
    }
}
