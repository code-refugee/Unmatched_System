package com.unmatched.service.impl;

import com.unmatched.pojo.User;
import com.unmatched.service.UseRestApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UseRestApiServiceImpl implements UseRestApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void postUser() {

    }

    @Override
    public void putUser() {

    }

    @Override
    public void useExchange() {

    }
}
