package com.whatstodo.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class HttpClient {
	private static final String TAG = "HttpClient";

	// Used for sending an existing resource to the server and get the updated
	// version
	public static JsonElement sendHttpPost(String URL, String jsonString)
			throws SynchronizationException {

		try {

			HttpPost httpPost = new HttpPost(URL);

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			StringEntity se;
			se = new StringEntity(jsonString);

			httpPost.setEntity(se);
			setJsonHeader(httpPost);

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient.execute(httpPost,
					localContext);

			Log.i(TAG,
					"HTTPResponse received in ["
							+ (System.currentTimeMillis() - t) + "ms]");

			return getJsonFromResponse(response);

		} catch (ClientProtocolException e) {
			throw new SynchronizationException(e);
		} catch (IOException e) {
			throw new SynchronizationException(e);
		} catch (JsonParseException e) {
			throw new SynchronizationException(e);
		}
	}

	// used to get a resource from the server
	public static JsonElement sendHttpGet(String URL)
			throws SynchronizationException {

		try {

			HttpGet httpGet = new HttpGet(URL);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			setJsonHeader(httpGet);

			HttpResponse response = httpClient.execute(httpGet, localContext);

			return getJsonFromResponse(response);

		} catch (ClientProtocolException e) {
			throw new SynchronizationException(e);
		} catch (IOException e) {
			throw new SynchronizationException(e);
		} catch (JsonParseException e) {
			throw new SynchronizationException(e);
		}
	}

	// Used to create a new resource on the server side
	public static void sendHttpPut(String URL, String jsonString)
			throws SynchronizationException {

		try {

			HttpPut httpPut = new HttpPut(URL);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			setJsonHeader(httpPut);

			HttpResponse response = httpClient.execute(httpPut, localContext);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() != HttpStatus.SC_NO_CONTENT) {

				throw new SynchronizationException(
						"Status Code of Put request was: "
								+ statusLine.getStatusCode() + ". Reason: "
								+ statusLine.getReasonPhrase());
			}

		} catch (ClientProtocolException e) {
			throw new SynchronizationException(e);
		} catch (IOException e) {
			throw new SynchronizationException(e);
		}
	}

	// Used to delete a resource on the server side
	public static void sendHttpDelete(String URL)
			throws SynchronizationException {

		try {

			HttpDelete httpDelete = new HttpDelete(URL);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			setJsonHeader(httpDelete);

			HttpResponse response = httpClient
					.execute(httpDelete, localContext);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() != HttpStatus.SC_NO_CONTENT) {

				String errorMessage = "Status Code of PUT request was: "
						+ statusLine.getStatusCode() + ". Reason: "
						+ statusLine.getReasonPhrase();
				
				throw new SynchronizationException(errorMessage);
			}

		} catch (ClientProtocolException e) {
			throw new SynchronizationException(e);
		} catch (IOException e) {
			throw new SynchronizationException(e);
		}
	}

	private static void setJsonHeader(HttpRequestBase request) {
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		request.setHeader("Accept-Encoding", "gzip");
	}

	private static JsonElement getJsonFromResponse(HttpResponse response)
			throws IOException {

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			// Read the content stream
			InputStream instream = entity.getContent();
			Header contentEncoding = response
					.getFirstHeader("Content-Encoding");
			if (contentEncoding != null
					&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}

			// convert content stream to a String
			String resultString = convertStreamToString(instream);
			instream.close();

			JsonParser parser = new JsonParser();
			JsonObject jsonObjRecv = parser.parse(resultString)
					.getAsJsonObject();

			// Raw DEBUG output of our received JSON object:
			Log.i(TAG, "<JSONObject>\n" + jsonObjRecv.toString()
					+ "\n</JSONObject>");

			return jsonObjRecv;
		} else {
			return JsonNull.INSTANCE;

		}
	}

	private static String convertStreamToString(InputStream is) {

		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
