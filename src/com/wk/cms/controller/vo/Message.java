package com.wk.cms.controller.vo;

public class Message {

	private boolean success;
	private String message;
	private Object obj;
	
	public Message() {
		
	}
	
	public Message(boolean success, String message, Object obj) {
		super();
		this.success = success;
		this.message = message;
		this.obj = obj;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
