package com.thirdeye30.resumehelper.messagebroker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye30.resumehelper.messagebroker.dtos.Message;
import com.thirdeye30.resumehelper.messagebroker.services.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messagebroker/message")
@RequiredArgsConstructor
public class MessageController {
	
    private final MessageService messageService;

    @GetMapping("/{topicname}/{topickey}")
    public ResponseEntity<Message> getMessage(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey) {
        
        Message message = messageService.getMessage(topicName, topicKey);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/multiple/{topicname}/{topickey}/{count}")
    public ResponseEntity<List<Message>> getMultipleMessage(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey, 
            @PathVariable("count") Long count) {
        
        List<Message> messages = messageService.getMessages(topicName, topicKey, count);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/{topicname}/{topickey}")
    public ResponseEntity<String> setMessage(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey, 
            @RequestBody Object message) {
            
        messageService.setMessage(topicName, topicKey, message);
        return new ResponseEntity<>("Message is added in topic", HttpStatus.CREATED);
    }
    
    @PostMapping("/multiple/{topicname}/{topickey}")
    public ResponseEntity<String> setMessages(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey, 
            @RequestBody List<Object> messages) {
            
        messageService.setMessages(topicName, topicKey, messages);
        return new ResponseEntity<>("Messages are added in topic", HttpStatus.CREATED);
    }
    
    @GetMapping("/aiintegreater/multiple/{topicname}/{topickey}/{count}")
    public ResponseEntity<List<Message>> getAiMultipleMessage(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey, 
            @PathVariable("count") Long count) {
        
        List<Message> messages = messageService.getAiMessages(topicName, topicKey, count);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/aiintegreater/multiple/{topicname}/{topickey}")
    public ResponseEntity<String> setAiMessages(
            @PathVariable("topicname") String topicName, 
            @PathVariable("topickey") String topicKey, 
            @RequestBody List<Object> messages) {
            
        messageService.setAiMessages(topicName, topicKey, messages);
        return new ResponseEntity<>("Messages are added in topic", HttpStatus.CREATED);
    }
}