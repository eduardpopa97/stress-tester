package com.app.backend.gui.view;

public class HttpParamClass {

	private String HttpParamName;
	private String HttpParamValue;
	
	public String getHttpParamName() {
		return HttpParamName;
	}
	
	public void setHttpParamName(String httpParamName) {
		HttpParamName = httpParamName;
	}
	
	public String getHttpParamValue() {
		return HttpParamValue;
	}
	
	public void setHttpParamValue(String httpParamValue) {
		HttpParamValue = httpParamValue;
	}
	
	public HttpParamClass(String httpParamName, String httpParamValue) {
		this.HttpParamName = httpParamName;
		this.HttpParamValue = httpParamValue;
	}
	
	public HttpParamClass() {
		
	}
	
	
}
