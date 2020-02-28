package com.unmatched.service.impl;

import com.unmatched.mapper.UserDao;
import com.unmatched.pojo.User;
import com.unmatched.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public User selectByPrimaryKey(int id){
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public void deleteByPrimaryKey(int id){
        userDao.deleteByPrimaryKey(id);
    }
}
