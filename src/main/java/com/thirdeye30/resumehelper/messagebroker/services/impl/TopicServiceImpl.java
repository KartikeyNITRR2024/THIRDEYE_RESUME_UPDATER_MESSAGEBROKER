package com.thirdeye30.resumehelper.messagebroker.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.thirdeye30.resumehelper.messagebroker.dtos.Topic;
import com.thirdeye30.resumehelper.messagebroker.services.QueueService;
import com.thirdeye30.resumehelper.messagebroker.services.TopicService;
import com.thirdeye30.resumehelper.messagebroker.utils.CodeGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final Map<String, Topic> topicMap = new ConcurrentHashMap<>();

    private final QueueService queueService;
    private final CodeGenerator codeGenerator;

    @Value("${thirdeye.maximumqueuesize}")
    private Long maximumQueueSize;

    @Value("${thirdeye.maximumqueues}")
    private Long maximumQueues;

    @Override
    public Topic addTopic(String topicName, Long maxLength) {
        log.info("Attempting to add topic: {}, with maxLength: {}", topicName, maxLength);
        if (topicMap.size() >= maximumQueues) {
            log.error("Cannot add topic {}: Maximum number of topics ({}) exceeded", topicName, maximumQueues);
            throw new RuntimeException("Maximum topic exceeds");
        }
        if (maxLength > maximumQueueSize) {
            log.error("Invalid maxLength {} for topic {}: Cannot exceed {}", maxLength, topicName, maximumQueueSize);
            throw new RuntimeException("Maximum size is invalid. Not greater than " + maximumQueueSize + ".");
        }
        if (topicMap.containsKey(topicName)) {
            log.error("Topic {} already exists", topicName);
            throw new RuntimeException("Topic allready exists");
        }
        String topicKey = codeGenerator.generateUniqueCode(8);
        queueService.addQueue(topicName);
        Topic topic = new Topic(topicName, topicKey, maxLength);
        topicMap.put(topicName, topic);
        log.info("Topic {} added successfully with key {}", topicName, topicKey);
        return topic;
    }

    @Override
    public boolean isTopicPresent(String topicName, String topicKey) {
        boolean present = topicMap.containsKey(topicName) && topicMap.get(topicName).getTopicKey().equals(topicKey);
        log.debug("Checking if topic {} with key {} is present: {}", topicName, topicKey, present);
        return present;
    }

    @Override
    public boolean isTopicFull(String topicName) {
        int currentSize = queueService.getSizeOfQueue(topicName);
        boolean full = currentSize >= topicMap.get(topicName).getMaxSize();
        log.debug("Checking if topic {} is full: {}", topicName, full);
        return full;
    }

    @Override
    public boolean isTopicEmpty(String topicName) {
        boolean empty = queueService.getSizeOfQueue(topicName) == 0;
        log.debug("Checking if topic {} is empty: {}", topicName, empty);
        return empty;
    }

    @Override
    public void emptyTopic(String topicName, String topicKey) {
        log.info("Clearing topic: {}, key: {}", topicName, topicKey);
        if (!isTopicPresent(topicName, topicKey)) {
            log.error("Failed to clear topic {}: Invalid key {}", topicName, topicKey);
            throw new RuntimeException("Invalid topic request");
        }
        queueService.clearQueue(topicName);
        log.info("Topic {} cleared successfully", topicName);
    }
    
    @Override
    public void emptyAllTopic() {
        log.info("Clearing all topics: {}", topicMap.size());
        for(String key : topicMap.keySet())
        {
        	Topic topic = topicMap.get(key);
        	emptyTopic(topic.getTopicName(), topic.getTopicKey());
        }
        log.info("Cleared all topis successfully");
    }

    @Override
    public void emptyMap() {
        log.info("Clearing all topics and their queues");
        queueService.clearMap();
        topicMap.clear();
        log.info("All topics cleared successfully");
    }

    @Override
    public void removeTopic(String topicName, String topicKey) {
        log.info("Removing topic: {}, key: {}", topicName, topicKey);
        if (!isTopicPresent(topicName, topicKey)) {
            log.error("Failed to remove topic {}: Invalid key {}", topicName, topicKey);
            throw new RuntimeException("Invalid topic request");
        }
        queueService.removeQueue(topicName);
        topicMap.remove(topicName);
        log.info("Topic {} removed successfully", topicName);
    }
}
