package com.thirdeye30.resumehelper.messagebroker.services;

import java.util.List;

import com.thirdeye30.resumehelper.messagebroker.dtos.Message;

public interface MessageService {

	Message getMessage(String topicName, String topicKey);
	
	List<Message> getMessages(String topicName, String topicKey, Long count);

	void setMessage(String topicName, String topicKey, Object message);
	
	void setMessages(String topicName, String topicKey, List<Object> messages);

	List<Message> getAiMessages(String topicName, String topicKey, Long count);

	void setAiMessages(String topicName, String topicKey, List<Object> messages);
}

