package com.machinememos.javaland.demo;

import java.io.IOException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatDogImageLoader {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean loaded = false;
	private byte[] imageBytes;
	private String imageUrl;

	public CatDogImageLoader(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static CatDogImageLoader forUrl(String imageUrl) {
		return new CatDogImageLoader(imageUrl);
	}
	
	public String getUrl(){
		return this.imageUrl;
	}
	
	public String getBase64(){
		return new String(Base64.encodeBase64(imageBytes));
	}

	public void load() {

		try {
			
			setImageBytes(ImageRetriever.getPNGBytes(imageUrl));
			setLoaded(true);

		} catch (IOException ex) {
			logger.debug("Unable to load image, possibly not a proper url.", ex);
		}
		
	}
	

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
