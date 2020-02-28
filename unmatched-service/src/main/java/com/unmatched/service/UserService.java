package com.unmatched.service;

import com.unmatched.pojo.User;
import org.springframework.cache.annotation.Cacheable;

public interface UserService {


    int insert(User user);

    //默认的缓存key要基于方法的参数来确定,此处的key是id
    @Cacheable(value = "userCache",key = "#id")
    User selectByPrimaryKey(int id);

    void deleteByPrimaryKey(int id);
}
