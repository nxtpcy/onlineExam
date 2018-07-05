package com.uestc.www.common;

public class Response {
	
	protected int status; // 响应状态
	protected Object body; // 响应主体
	protected String message; // 响应信息

	public Response(int status) {
		this.status = status;
	}

	public Response(int status, String message) {
		this(status);
		this.message = message;
	}

	public Response(int status, Object body) {
		this(status);
		this.body = body;
	}

	public Response(int status, String message, Object body) {
		this(status, message);
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
