package com.nautilusapps.RestDroid;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;


public class RestDroid {

	// request.addHeader("Accept", "application/json");
	// request.addHeader("Content-Type", "application/json");

	public static RestResponse Get(String url) {
		HttpGet request = new HttpGet(url);
		return RestConnector.getSharedInstance().execute(request);
	}

	private static UrlEncodedFormEntity CompilePostData(String... params) throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String param : params) {
			String[] parts = param.split("=");
			if (parts.length == 2)
				nameValuePairs.add(new BasicNameValuePair(parts[0], parts[1]));
		}
		return new UrlEncodedFormEntity(nameValuePairs);
	}

	public static RestResponse Post(String url, String... params) {
		HttpPost request = new HttpPost(url);
		try {
			if (params != null && params.length > 0)
				request.setEntity(CompilePostData(params));
		} catch (UnsupportedEncodingException e) {
			RestResponse response = new RestResponse();
			response.setException(e);
			return response;
		}
		return RestConnector.getSharedInstance().execute(request);
	}

	public static RestResponse Put(String url, String... params) {
		HttpPut request = new HttpPut(url);
		try {
			if (params != null && params.length > 0)
				request.setEntity(CompilePostData(params));
		} catch (UnsupportedEncodingException e) {
			RestResponse response = new RestResponse();
			response.setException(e);
			return response;
		}
		return RestConnector.getSharedInstance().execute(request);
	}

	public static RestResponse Delete(String url) {
		HttpDelete request = new HttpDelete(url);
		return RestConnector.getSharedInstance().execute(request);
	}

}
