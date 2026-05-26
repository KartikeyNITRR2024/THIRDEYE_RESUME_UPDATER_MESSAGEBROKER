package com.thirdeye30.resumehelper.messagebroker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye30.resumehelper.messagebroker.dtos.Topic;
import com.thirdeye30.resumehelper.messagebroker.services.TopicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messagebroker/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    
    @PostMapping("/{topicname}/{maxlength}")
    public ResponseEntity<Topic> createTopic(
            @PathVariable("topicname") String topicName, 
            @PathVariable("maxlength") Long maxLength) {
        
        Topic newTopic = topicService.addTopic(topicName, maxLength);
        return new ResponseEntity<>(newTopic, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{topicname}/{topickey}")
    public ResponseEntity<String> deleteTopic(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey) {
        
        topicService.removeTopic(topicName, topicKey);
        return ResponseEntity.ok("Topic removed successfully");
    }
    
    @PostMapping("remove/{topicname}/{topickey}")
    public ResponseEntity<String> removeTopic(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey) {
        
        topicService.emptyTopic(topicName, topicKey);
        return ResponseEntity.ok("Topic emptied successfully");
    }
}