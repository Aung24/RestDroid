package com.nautilusapps.RestDroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


/*
 * Response from rest call is contained.
 * RestResponse will automatically parse the HTTP body results into a JSON array, if possible.
 */
public class RestResponse {
	public static final String TAG = "RestResponse";

	public Exception exception;
	public HttpResponse response;
	public boolean error;
	public File destination;
	public ProgressDelegate delegate;
	public JSONArray jsonArray;
	public String body;
	public int statusCode;

	public RestResponse() {
		// Empty response only contains an error
		this.error = true;
		this.destination = null;
		this.delegate = null;
	}
	
	public RestResponse(File destination, ProgressDelegate delegate) {
		// Empty response only contains an error
		this.error = true;
		this.destination = destination;
		this.delegate = delegate;
	}

	public void setResponse(HttpResponse response) {
		try {
			this.response = response;
			this.error = false;
			this.statusCode = response.getStatusLine().getStatusCode();

			long length = response.getEntity().getContentLength();
			InputStream content = response.getEntity().getContent();
			if (destination != null) {

				FileOutputStream fs = FileUtils.openOutputStream(destination);
				byte[] buffer = new byte[4096];
				int len1 = 0;
				long total = 0;
				while ((len1 = content.read(buffer)) > 0) {
					total += len1; // total = total + len1
					fs.write(buffer, 0, len1);

					if (delegate != null) {
						delegate.updateProgress((int) ((total * 100) / length));

						// Did we get stopped?
						if (delegate.hasCanceled())
							break;
					}
				}
				fs.close();

			} else {
				// Extract string data from HTTP response
				try {
					this.body = convertStreamToString(content);
					content.close();

					// Log HTTP response
					Log.d(TAG, "Response: " + statusCode + " :: " + this.body);

					// Try first to convert to a JSON array as an array
					if (body != null && body != "") {
						try {
							this.jsonArray = new JSONArray(body);
						} catch (JSONException e) {
							try {
								// Next try to convert as a JSON object
								this.jsonArray = new JSONArray().put(new JSONObject(body));
							} catch (JSONException e2) {
								// Dang, couldn't make sense of the body
								setException(e2);
								Log.d(TAG, "Could not parse body content as JSON: " + body);
							}
						}
					}
				} catch (IllegalStateException e) {
					setException(e);
				} catch (IOException e) {
					setException(e);
				}
			}
		} catch (Exception e) {
			setException(e); // don't kill the app on error, just pass on the
								// exception
		}
	}

	public void setException(Exception exception) {
		Log.d(TAG, "Exception: " + exception.toString());
		this.exception = exception;
		this.error = true;
	}

	@Override
	public String toString() {
		if (error)
			return "Error: " + exception.toString();
		return "Status: " + statusCode + " :: Body: " + body;
	}

	// Helper method to consume HTTP result body
	private static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

}
