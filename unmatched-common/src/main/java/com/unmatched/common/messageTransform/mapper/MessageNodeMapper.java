package com.unmatched.common.messageTransform.mapper;

import com.unmatched.common.messageTransform.entity.XMLMessageNodeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageNodeMapper {

    List<XMLMessageNodeInfo> findXMLNodeByUseFor(@Param("useFor") String useFor);

}
