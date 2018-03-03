package com.machinememos.javaland.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeClassificationController {

	private static final String IMAGE_PREFIX = "data:image/jpeg;base64,";
	private static final String MESSAGE_LOADED = "Image downloaded and sent to kafka cluster";
	private static final String MESSAGE_UNABLE_TO_LOAD = "Unable to download image. Check if your url is correct";
	
	@Autowired
	private KafkaMessageProducer kafkaMessageProducer;
	
	@Autowired
	private ImageConverter imageConverter;
	
    @MessageMapping("/webcamimage")
    @SendTo("/topic/realtimeclassification")
    public CatDogWebSocketDTO hangleUserMessage(String imageDataUrl) throws Exception {
		CatDogWebSocketDTO catDogWebSocketDTO = new CatDogWebSocketDTO();
		
		if ( imageDataUrl.startsWith(IMAGE_PREFIX)){
			
			String originalImageBase64JPEG = imageDataUrl.substring(IMAGE_PREFIX.length());
			String resizedImageBase64PNG = imageConverter.toBase64EncodedPNG(originalImageBase64JPEG);
			kafkaMessageProducer.publish(resizedImageBase64PNG);
		
		}
		catDogWebSocketDTO.setLabel("hello");
		return catDogWebSocketDTO;
    }
	
    
		
}
