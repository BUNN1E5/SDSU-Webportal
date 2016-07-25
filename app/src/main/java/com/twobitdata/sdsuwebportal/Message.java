package com.twobitdata.sdsuwebportal;

public class Message{

	public String subject;
	public String from;
	public String link;
	
	public Message(String subject, String from){
		this.from = from;
		this.subject = subject;
	}
	
	public Message(String subject, String from, String link){
		this.from = from;
		this.subject = subject;
		this.link = link;
	}
	
	public String getMessage(){
		return "Uh oh, this is currently not supported!";
	}
	
}
