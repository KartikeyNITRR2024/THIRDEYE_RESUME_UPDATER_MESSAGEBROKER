package com.thirdeye30.resumehelper.messagebroker.services.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye30.resumehelper.messagebroker.utils.TimeManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.thirdeye30.resumehelper.messagebroker.dtos.Message;
import com.thirdeye30.resumehelper.messagebroker.services.MessageService;
import com.thirdeye30.resumehelper.messagebroker.services.QueueService;
import com.thirdeye30.resumehelper.messagebroker.services.TopicService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final QueueService queueService;
    private final TopicService topicService;
    private final TimeManager timeManager;
    
    @Override
    public Message getMessage(String topicName, String topicKey) {
        log.info("Fetching a message from topic: {}, with key: {}", topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            log.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new RuntimeException("Invalid message request");
        }
        if (topicService.isTopicEmpty(topicName)) {
            log.warn("Topic {} is empty", topicName);
            throw new RuntimeException("Topic is empty");
        }
        Message message = queueService.getMessage(topicName);
        log.info("Retrieved message from topic: {}", topicName);
        return message;
    }

    @Override
    public void setMessage(String topicName, String topicKey, Object message) {
        log.info("Adding a message to topic: {}, key: {}", topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            log.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new RuntimeException("Invalid message request");
        }
        Message newMessage = new Message(timeManager.getCurrentTime(), message);
        if (topicService.isTopicFull(topicName)) {
            log.warn("Topic {} is full, removing oldest message", topicName);
            queueService.removeAndSetMessage(topicName, newMessage);
        }
        queueService.setMessage(topicName, newMessage);
        log.info("Message added to topic: {}", topicName);
    }

    @Override
    public List<Message> getMessages(String topicName, String topicKey, Long count) {
        log.info("Fetching {} messages from topic: {}, key: {}", count, topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            log.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new RuntimeException("Invalid message request");
        }
        if (topicService.isTopicEmpty(topicName)) {
            log.warn("Topic {} is empty", topicName);
            throw new RuntimeException("Topic is empty");
        }
        List<Message> messages = new ArrayList<>();
        long present = Math.min(count, queueService.getSizeOfQueue(topicName));
        for (long i = 0; i < present; i++) {
            messages.add(queueService.getMessage(topicName));
        }
        log.info("Retrieved {} messages from topic: {}", messages.size(), topicName);
        return messages;
    }

    @Override
    public void setMessages(String topicName, String topicKey, List<Object> messages) {
        log.info("Adding {} messages to topic: {}, key: {}", messages.size(), topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            log.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new RuntimeException("Invalid message request");
        }
        for (Object message : messages) {
            Message newMessage = new Message(timeManager.getCurrentTime(), message);
            if (topicService.isTopicFull(topicName)) {
                log.warn("Topic {} is full, removing oldest message", topicName);
                queueService.removeAndSetMessage(topicName, newMessage);
            }
            queueService.setMessage(topicName, newMessage);
        }
        log.info("Added {} messages to topic: {}", messages.size(), topicName);
    }
}

