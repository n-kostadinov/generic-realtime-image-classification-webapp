//package com.machinememos.javaland.demo;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Properties;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.tomcat.util.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class KafkaScheduledConsumer {
//
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	private static final String KAFKA_LABEL_TOPIC = "catdoglabel";
//	private static final Properties properties = new Properties();
//	static{
//		properties.put("bootstrap.servers", "localhost:9092");
//		properties.put("group.id", "catdogwebapp");
//		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//	}
//
//	private List<String> knownUrls = new ArrayList<>();
//
//	@Autowired
//	private SimpMessagingTemplate template;
//	private KafkaConsumer<String, String> kafkaConsumer;
//
//	@PostConstruct
//	public void initilize() {
//		logger.info("initilize");
//		kafkaConsumer = new KafkaConsumer<>(properties);
//		kafkaConsumer.subscribe(Arrays.asList(KAFKA_LABEL_TOPIC));
//	}
//
//	@Scheduled(fixedRate = 100)
//	public void consume() {
//		
//		ConsumerRecords<String, String> records = kafkaConsumer.poll(0L);
//		Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
//		
//		while(iterator.hasNext()){
//			ConsumerRecord<String, String> consumerRecord = iterator.next();
//			safelyProcessRecord(consumerRecord);
//		}
//
//	}
//
//	private void safelyProcessRecord(ConsumerRecord<String, String> consumerRecord) {
//		try {
//			processRecord(consumerRecord);
//		} catch (IOException ex) {
//			throw new IllegalStateException("Unable to parse", ex);
//		}
//	}
//
//	private void processRecord(ConsumerRecord<String, String> consumerRecord)
//			throws IOException, JsonParseException, JsonMappingException {
//		CatDogKafkaDTO catDogEvent = new ObjectMapper().readValue(consumerRecord.value(), CatDogKafkaDTO.class);
//		
//		if(!knownUrls.contains(catDogEvent.getUrl())){
//			logger.info("New catdog event with label: " + catDogEvent.getLabel());
//			CatDogWebSocketDTO catDogDTO = new CatDogWebSocketDTO();
//			catDogDTO.setUrl(new String(Base64.decodeBase64(catDogEvent.getUrl())));
//			catDogDTO.setLabel(catDogEvent.getLabel());
//			catDogDTO.setResolved(true);
//			template.convertAndSend("/topic/catdog", catDogDTO);
//		}
//	}
//
//}
