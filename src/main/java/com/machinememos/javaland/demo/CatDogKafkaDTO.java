package com.machinememos.javaland.demo;

import org.apache.tomcat.util.codec.binary.Base64;

public class CatDogKafkaDTO {

	private String url; 
	private String label;
	private String data;
	
	
	public static CatDogKafkaDTO base64CatDogEvent(String url, byte[] data){
		CatDogKafkaDTO catDogEvent = new CatDogKafkaDTO();
		catDogEvent.setData(new String(Base64.encodeBase64(data)));
		catDogEvent.setUrl(new String(Base64.encodeBase64URLSafe(url.getBytes())));
		return catDogEvent;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	
}
