package com.machinememos.javaland.demo;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageProducer {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${kafka.brokeraddress}")
	private String kafkaBrokerAddress;
	
	@Value("${kafka.imagetopic}")
	private String kafkaImageTopic;
	
	private Producer<String, String> kafkaProducer;
	
	@PostConstruct
	private void initialize() {
		
//		Properties properties = new Properties();
//		properties.put("bootstrap.servers", kafkaBrokerAddress);
//		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
//		kafkaProducer = new KafkaProducer<>(properties);
	}	
	
	public void publish(String pngImage) {
		logger.info("Sending encoded image " + pngImage.length());
//		this.kafkaProducer.send(new ProducerRecord<String, String>(kafkaImageTopic, pngImage));
	}
	
	@PreDestroy
	public void close() {
//		this.kafkaProducer.close();
	}
	
}
