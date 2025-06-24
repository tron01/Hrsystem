package com.Abhijith.application_submit_service.kafka.producer;

import com.Abhijith.application_submit_service.kafka.event.ResumeParseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeParseProducer {

	private final KafkaTemplate<String, ResumeParseEvent> kafkaTemplate;
	
	@Value("${kafka.topics.resume-parse}")
	private String resumeParseTopic;
	
	public void sendResumeParseEvent(ResumeParseEvent event) {
		CompletableFuture<SendResult<String, ResumeParseEvent>> future =
				kafkaTemplate.send(resumeParseTopic, event);
		
		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Failed to send Kafka event for applicationId {}: {}", event.getApplicationId(), ex.getMessage(), ex);
			} else {
				log.info("Kafka message sent: topic={}, partition={}, offset={}",
						result.getRecordMetadata().topic(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
			}
		});
	}

}
