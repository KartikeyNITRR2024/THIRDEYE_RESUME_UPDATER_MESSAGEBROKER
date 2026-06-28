package com.thirdeye30.resumehelper.messagebroker.utils;

import org.springframework.stereotype.Component;
import com.thirdeye30.resumehelper.messagebroker.services.TopicService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initiatier {
	
    private final TopicService topicService;
    private Boolean isFirstTime = true;
    
	@PostConstruct
    public void init() throws Exception{
        log.info("Initializing Initiatier...");
        if(isFirstTime)
        {
        	topicService.addTopic("textextracter", 1000L);
        	topicService.addTopic("aiprocesser", 1000L);
        	topicService.addTopic("aiprocesser2", 1000L);
        	topicService.addTopic("priorityskills", 1000L);
        	topicService.addTopic("statusupdater", 1000L);
        	topicService.addTopic("mailprocesser", 1000L);
        	topicService.addTopic("courseprocesser", 1000L);
        	isFirstTime = false;
        }
        log.info("Initiatier initialized.");
    }
	
	public void refreshMemory()
	{
		log.info("Going to refersh memory...");
		topicService.emptyAllTopic();
		log.info("Memory refreshed.");
	}

}


