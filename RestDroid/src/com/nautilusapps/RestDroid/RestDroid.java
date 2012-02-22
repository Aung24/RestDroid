package com.nautilusapps.RestDroid;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;


public class RestDroid {
	public static final String TAG = "RestDroid";

	public static RestResponse Get(String url) {
		HttpGet request = new HttpGet(url);
		return RestConnector.getSharedInstance().execute(request, null);
	}

	public static RestResponse Get(String url, File destination, ProgressDelegate delegate)
			throws IOException {
		HttpGet request = new HttpGet(url);
		
		RestResponse response = new RestResponse(destination, delegate);
		RestConnector.getSharedInstance().execute(request, response);
		
		return response;
	}

	private static UrlEncodedFormEntity CompilePostData(String... params)
			throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String param : params) {
			int i = param.indexOf("=");
			if (i > 0) {
				String key = param.substring(0, i);
				String value = param.substring(i + 1);
				if (key != null && value != null)
					nameValuePairs.add(new BasicNameValuePair(key, value));
			}
		}
		return new UrlEncodedFormEntity(nameValuePairs);
	}

	public static RestResponse Post(String url, ProgressDelegate delegate, String filePartName,
			File file, String... params) {
		HttpPost request = new HttpPost(url);
		try {
			ProgressMultipartEntity entity = new ProgressMultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE, delegate);
			for (String param : params) {
				int i = param.indexOf("=");
				if (i > 0) {
					String key = param.substring(0, i);
					String value = param.substring(i + 1);
					if (key != null && value != null)
						entity.addPart(key, new StringBody(value));
				}
			}
			if (file != null)
				entity.addPart(filePartName, new FileBody(file));
			request.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			RestResponse response = new RestResponse();
			response.setException(e);
			return response;
		}
		return RestConnector.getSharedInstance().execute(request, null);
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
		return RestConnector.getSharedInstance().execute(request, null);
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
		return RestConnector.getSharedInstance().execute(request, null);
	}

	public static RestResponse Delete(String url) {
		HttpDelete request = new HttpDelete(url);
		return RestConnector.getSharedInstance().execute(request, null);
	}

}
