package com.machinememos.javaland.demo;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KafkaScheduledConsumer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${kafka.topic.classificationlabel}")
	private String kafkaLabelTopic;
	
	@Value("${kafka.address}")
	private String kafkaAddress;
	
	@Value("${websocket.topic.realtimeclassification}")
	private String websocketTopic;

	@Autowired
	private SimpMessagingTemplate template;
	private KafkaConsumer<String, String> kafkaConsumer;

	@PostConstruct
	public void initilize() {
		
		Properties properties = new Properties();
		properties.put("bootstrap.servers", kafkaAddress);
		properties.put("group.id", "group1");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		kafkaConsumer = new KafkaConsumer<>(properties);
		kafkaConsumer.subscribe(Arrays.asList(kafkaLabelTopic));
		
	}

	@Scheduled(fixedRate = 50)
	public void consume() {
		
		ConsumerRecords<String, String> records = kafkaConsumer.poll(0L);
		StreamSupport.stream(records.spliterator(), false)
			.forEach(this::sendRecordOverWebsocket);
		
	}

	private void sendRecordOverWebsocket(ConsumerRecord<String, String> consumerRecord) {
		
		logger.info("Record send.");
		template.convertAndSend(websocketTopic, consumerRecord.value());
		
	}

}
