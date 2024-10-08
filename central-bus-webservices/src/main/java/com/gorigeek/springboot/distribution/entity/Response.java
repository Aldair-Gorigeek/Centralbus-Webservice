package com.gorigeek.springboot.distribution.entity;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class Response<T> {
	private int code;
	private T body;

	public Response() {
		super();
	}
	
	public Response(int code, String message) {
		this.code = code;		
	}

	public Response(int code, String message, T body) {
		this.code = code;		
		this.body = body;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}	
}
