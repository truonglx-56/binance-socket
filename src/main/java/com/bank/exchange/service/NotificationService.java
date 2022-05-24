package com.bank.exchange.service;

import com.bank.exchange.message.Message;

public interface NotificationService {

    void notify(String topic, Message message);
}
