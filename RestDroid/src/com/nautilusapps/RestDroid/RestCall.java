package com.nautilusapps.RestDroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


/*
 * Used to describe a rest call to a server.
 */
public class RestCall {
	public static final String TAG = "RestCall";

	public enum Action {
		GET, PUT, POST, DELETE;
	}

	public enum ContentType {
		FORM_DATA, JSON, MULTIPART;
	}

	private String url;
	private Action action;
	private ContentType contentType;
	private String postValue;
	private String params;

	public RestCall(String url) {
		this.url = url;
		this.action = Action.GET;
		this.contentType = ContentType.FORM_DATA;
		this.postValue = "";
		this.params = "";
	}

	public RestCall(Action action, String url) {
		this(url);
		this.action = action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void setPostValue(String postValue) {
		this.contentType = ContentType.FORM_DATA;
		this.postValue = postValue;
	}
	
	public void addParam(String name, String value) {
		String newParam = URLEncoder.encode(name) + "=" + URLEncoder.encode(value);
		if (!"".equals(params))
			params += "&" + newParam;
		else
			params += newParam;
	}

	public void addJsonNVP(String name, String value) throws JSONException {
//		contentType = ContentType.JSON;
//		if (paramData == null)
//			paramData = new JSONObject();
//		paramData.put(name, value);
	}

	public HttpRequestBase getRequest() throws UnsupportedEncodingException {
		HttpRequestBase request = null;
		switch (action) {
		case GET:
			request = new HttpGet(getUrl());
			break;
		case POST:
			request = new HttpPost(getUrl());
			if (contentType == ContentType.JSON) {
//				((HttpPost) request).setEntity(new StringEntity(paramData.toString()));
			} else {
				((HttpPost) request).setEntity(new StringEntity(postValue));
			}
			break;
		}
		addRequestHeaders(request);
		return request;
	}

	private String getUrl() {
		if (!"".equals(params))
			return url + "?" + params;
		return url;
	}

	private void addRequestHeaders(HttpRequestBase request) {
		if (contentType == ContentType.JSON) {
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-Type", "application/json");
		}
	}

}
