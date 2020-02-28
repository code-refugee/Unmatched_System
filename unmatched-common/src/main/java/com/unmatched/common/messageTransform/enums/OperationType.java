package com.unmatched.common.messageTransform.enums;

/**
* Description: 枚举类型，用于定义所做的操作，如，记账，查证等
* @author: yuhang tao
* @date: 2019/12/23
* @version: v1.0
*/
public enum OperationType {

    CORE_ACCT_RECORD("1"),
    CORE_ACCT_ERASE("2");

    private String useFor;

    OperationType(String useFor){
        this.useFor = useFor;
    }

    public String getUseFor() {
        return useFor;
    }
}
