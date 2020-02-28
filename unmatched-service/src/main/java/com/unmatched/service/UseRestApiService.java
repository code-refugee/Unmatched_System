package com.unmatched.service;

import com.unmatched.pojo.User;

public interface UseRestApiService {

    User getUser();

    void deleteUser();

    void postUser();

    void putUser();

    void useExchange();
}
