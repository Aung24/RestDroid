package com.nautilusapps.RestDroid;

import org.apache.http.client.methods.HttpRequestBase;


/*
 * Used to describe a rest call to a server.
 */
public class RestCall {
	
	private String url;
	
	public RestCall(String url) {
		this.url = url;
	}
	
	public HttpRequestBase getRequest() {
		return null;
	}
	
}