package com.machinememos.javaland.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeClassificationController {

	private static final String IMAGE_PREFIX = "data:image/jpeg;base64,";
	
	@Autowired
	private KafkaMessageProducer kafkaMessageProducer;
	
	@Autowired
	private ImageConverter imageConverter;
	
    @MessageMapping("/webcamimage")
    public void hangleWebcamImage(String imageDataUrl) {
		
		if ( imageDataUrl.startsWith(IMAGE_PREFIX)){
			
			String originalImageBase64JPEG = imageDataUrl.substring(IMAGE_PREFIX.length());
			String resizedImageBase64PNG = imageConverter.toBase64EncodedPNG(originalImageBase64JPEG);
			kafkaMessageProducer.publish(resizedImageBase64PNG);
		
		}

    }
	
    
		
}
