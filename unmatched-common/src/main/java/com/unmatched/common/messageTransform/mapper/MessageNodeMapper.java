package com.unmatched.common.messageTransform.mapper;

import com.unmatched.common.messageTransform.entity.MessageNodeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageNodeMapper {
    List<MessageNodeInfo> findByUseFor(@Param("useFor") String useFor);
}
