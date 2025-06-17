package com.Abhijith.application_submit_service.kafka.producer;

import com.Abhijith.application_submit_service.kafka.event.ResumeParseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component

public class ResumeParseProducer {

	private final KafkaTemplate<String, ResumeParseEvent> kafkaTemplate;
	
	@Value("${kafka.topics.resume-parse}")
	private String resumeParseTopic;
	
	public ResumeParseProducer(KafkaTemplate<String, ResumeParseEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendResumeParseEvent(ResumeParseEvent event) {
		kafkaTemplate.send(resumeParseTopic, event);
	}

}
