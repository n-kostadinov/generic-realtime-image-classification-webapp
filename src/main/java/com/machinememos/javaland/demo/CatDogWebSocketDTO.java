package com.machinememos.javaland.demo;

public class CatDogWebSocketDTO {

    private String content;
    
    private boolean resolved = false;

    private String label;
    
    private String url;

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
    	this.content = content;
    }

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
