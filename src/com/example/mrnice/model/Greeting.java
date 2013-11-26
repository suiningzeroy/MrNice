package com.example.mrnice.model;

public class Greeting {
	
	private String id, title, content;
	
	public Greeting(){}
	
	@Override
	public String toString() {
		return title;
	}
	
	public String getId() {
			return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
