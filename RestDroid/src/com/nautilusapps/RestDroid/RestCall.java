package com.nautilusapps.RestDroid;

import org.apache.http.client.methods.HttpRequestBase;


/*
 * Used to describe a rest call to a server.
 */
public class RestCall {
	
	public enum Action {
		GET, PUT, POST, DELETE;
	}
	
	private String url;
	private Action action;
	
	public RestCall(String url) {
		this.url = url;
	}
	
	public RestCall(Action action, String url) {
		this.action = action;
		this.url = url;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public HttpRequestBase getRequest() {
		return null;
	}
	
}