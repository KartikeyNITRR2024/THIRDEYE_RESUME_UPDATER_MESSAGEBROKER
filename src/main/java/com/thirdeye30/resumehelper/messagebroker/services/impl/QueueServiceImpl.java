package com.thirdeye30.resumehelper.messagebroker.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Service;

import com.thirdeye30.resumehelper.messagebroker.dtos.Message;
import com.thirdeye30.resumehelper.messagebroker.services.QueueService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueueServiceImpl implements QueueService {

    private final Map<String, ConcurrentLinkedDeque<Message>> queueMap = new ConcurrentHashMap<>();

    @Override
    public void addQueue(String topicName) {
        log.info("Adding a new queue for topic: {}", topicName);
        queueMap.put(topicName, new ConcurrentLinkedDeque<>());
    }

    @Override
    public Message getMessage(String topicName) {
        log.info("Fetching a message from topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        Message message = deque.pollFirst();
        if (message == null) {
            log.warn("No message found in topic: {}", topicName);
        } else {
            log.info("Message retrieved from topic: {}", topicName);
        }
        return message;
    }

    @Override
    public void setMessage(String topicName, Message message) {
        log.info("Adding message to topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        deque.addLast(message);
        log.debug("Queue size for {} after adding: {}", topicName, deque.size());
    }

    @Override
    public void removeAndSetMessage(String topicName, Message message) {
        log.info("Replacing oldest message in topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        deque.pollFirst();
        deque.addLast(message);
        log.debug("Queue size for {} after replacement: {}", topicName, deque.size());
    }

    @Override
    public int getSizeOfQueue(String topicName) {
        int size = queueMap.get(topicName).size();
        log.debug("Queue size for {}: {}", topicName, size);
        return size;
    }

    @Override
    public int getSizeOfMap() {
        int size = queueMap.size();
        log.debug("Total number of queues: {}", size);
        return size;
    }

    @Override
    public void clearQueue(String topicName) {
        log.info("Clearing queue for topic: {}", topicName);
        queueMap.put(topicName, new ConcurrentLinkedDeque<>());
    }

    @Override
    public void clearMap() {
        log.info("Clearing all queues");
        queueMap.clear();
    }

    @Override
    public void removeQueue(String topicName) {
        log.info("Removing queue for topic: {}", topicName);
        queueMap.remove(topicName);
    }
}

