package com.app.backend.protocolController;

public class HttpResponseModel {

	private String latency;
	private String url;
	private String sentBytes;
	private String receivedBytes;
	private String thread;
	private String responseCode;
	private String responseMessage;
	
	public String getLatency() {
		return latency;
	}
	
	public void setLatency(String latency) {
		this.latency = latency;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(String sentBytes) {
		this.sentBytes = sentBytes;
	}

	public String getReceivedBytes() {
		return receivedBytes;
	}

	public void setReceivedBytes(String receivedBytes) {
		this.receivedBytes = receivedBytes;
	}
	
	public String getThread() {
		return thread;
	}
	
	public void setThread(String thread) {
		this.thread = thread;
	}
	public String getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public HttpResponseModel(String latency, String url, String thread, String responseCode, String responseMessage) {
		this.latency = latency;
		this.url = url;
		this.thread = thread;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	
	
	
}
