package com.machinememos.javaland.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CatDogController {

	private static final String IMAGE_PREFIX = "catdog:";
	private static final String MESSAGE_LOADED = "Image downloaded and sent to kafka cluster";
	private static final String MESSAGE_UNABLE_TO_LOAD = "Unable to download image. Check if your url is correct";
	
	@Autowired
	private KafkaMessageProducer messageProducer;
	
    @MessageMapping("/usermessage")
    @SendTo("/topic/catdog")
    public CatDogWebSocketDTO hangleUserMessage(UserMessage userMessage) throws Exception {
		CatDogWebSocketDTO catDogWebSocketDTO = new CatDogWebSocketDTO();
		
		if ( userMessage.getMessage().startsWith(IMAGE_PREFIX)){
			
			loadImageAndPrepareResponse(catDogWebSocketDTO, userMessage.getMessage());
			
		} else {
			catDogWebSocketDTO.setContent(userMessage.getMessage());
		}
		
		return catDogWebSocketDTO;
    }

	private void loadImageAndPrepareResponse(CatDogWebSocketDTO catDogWebSocketDTO, String userMessage) {
		
		String url = userMessage.replace(IMAGE_PREFIX, "");
		
		CatDogImageLoader loader = new CatDogImageLoader(url);
		loader.load();
		if(loader.isLoaded()){
			catDogWebSocketDTO.setContent(MESSAGE_LOADED);
			messageProducer.publish(CatDogKafkaDTO.base64CatDogEvent(url, loader.getImageBytes()));
		} else {
			catDogWebSocketDTO.setContent(MESSAGE_UNABLE_TO_LOAD);
		}
	}
	

}
