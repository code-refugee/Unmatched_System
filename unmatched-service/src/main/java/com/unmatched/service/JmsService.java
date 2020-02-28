package com.unmatched.service;

public interface JmsService {

    void sendUserToClient();

    void getInfoAsync(String msg);
}
