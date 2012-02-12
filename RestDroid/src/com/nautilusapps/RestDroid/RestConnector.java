package com.nautilusapps.RestDroid;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.util.Log;


/*
 * Used to execute RestCall objects.
 * RestConnector is a singleton, access it via the public static getSharedInstance()
 */
public class RestConnector {
	public static final String TAG = "RestConnector";

	public static final int DEFAULT_TIMEOUT = 3000;

	private CookieStore cookieJar;
	private int timeout;

	// Singleton management
	public static RestConnector sharedInstance = null;

	public static RestConnector getSharedInstance() {
		if (sharedInstance == null)
			sharedInstance = new RestConnector();
		return sharedInstance;
	}

	public RestConnector() {
		this.cookieJar = null; // don't set it up unless you need it
		this.timeout = DEFAULT_TIMEOUT;
	}

	public void setCookie(Cookie cookie) {
		if (cookieJar == null)
			cookieJar = new BasicCookieStore();
		cookieJar.addCookie(cookie);
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public CookieStore getCookieStore() {
		return cookieJar;
	}

	public RestResponse execute(RestCall call) {
		RestResponse restResponse = new RestResponse();

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		DefaultHttpClient client = new DefaultHttpClient(params);

		if (cookieJar != null) // Install cookies
			client.setCookieStore(cookieJar);

		try {
			HttpRequestBase request = call.getRequest();
			logCallDetails(request);
			HttpResponse response = client.execute(request);
			restResponse.setResponse(response);
		} catch (ClientProtocolException e) {
			restResponse.setException(e);
		} catch (IOException e) {
			restResponse.setException(e);
		}

		return restResponse;
	}

	private void logCallDetails(HttpRequestBase request) {
		int cookieCount = 0;
		if (cookieJar != null)
			cookieCount = cookieJar.getCookies().size();
		String message = request.getURI().toString() + " [" + timeout + "ms timeout - ";
		if (cookieCount > 0)
			message += cookieCount + " cookies]";
		else
			message += "]";
		Log.d(TAG, message);
	}

}
