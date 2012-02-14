package com.nautilusapps.RestDroid;

import java.io.UnsupportedEncodingException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;


public class RestDroid {

//			request.addHeader("Accept", "application/json");
//			request.addHeader("Content-Type", "application/json");
	
	public static RestResponse get(String url) {
		HttpGet request = new HttpGet(url);
		return RestConnector.getSharedInstance().execute(request);
	}

	public static RestResponse post(String url, String postData) {
		HttpPost request = new HttpPost(url);
		try {
			if (postData != null)
				request.setEntity(new StringEntity(postData));
		} catch (UnsupportedEncodingException e) {
			RestResponse response = new RestResponse();
			response.setException(e);
			return response;
		}
		return RestConnector.getSharedInstance().execute(request);
	}

	public static RestResponse post(String url) {
		return post(url, null);
	}

	public static RestResponse put(String url, String postData) {
		HttpPut request = new HttpPut(url);
		try {
			if (postData != null)
				request.setEntity(new StringEntity(postData));
		} catch (UnsupportedEncodingException e) {
			RestResponse response = new RestResponse();
			response.setException(e);
			return response;
		}
		return RestConnector.getSharedInstance().execute(request);
	}

	public static RestResponse delete(String url) {
		HttpDelete request = new HttpDelete(url);
		return RestConnector.getSharedInstance().execute(request);
	}

}
