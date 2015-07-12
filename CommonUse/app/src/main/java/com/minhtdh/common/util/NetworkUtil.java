package com.minhtdh.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Utilities for post, get data via HTTP connect require permission INTERNET
 * 
 * @author MinhTDH
 * 
 ********************************************************* 
 */
public class NetworkUtil {
	private static final int TIME_OUT_CONNECTION = 5000;
	private static final int TIME_OUT_SOCKET = 20000;
	public static final int BUFFER_SIZE = 5 * 1024;

	/**
	 * check if network available require permission ACCESS_NETWORK_STATE,
	 * ACCESS_WIFI_STATE
	 * 
	 * @author MinhTDH
	 * @param context
	 * @return boolean
	 ********************************************************* 
	 */
	public static boolean checkStatusNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo wifiNetwork = cm
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetwork != null && wifiNetwork.isConnected()) {
				return true;
			}
	
			NetworkInfo mobileNetwork = cm
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileNetwork != null && mobileNetwork.isConnected()) {
				return true;
			}
	
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				return true;
			}
		}
		return false;
	}

	public static HttpClient getDefaultHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, TIME_OUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(params, TIME_OUT_SOCKET);
		HttpClient client = new DefaultHttpClient(params);
		return client;
	}

	/**
	 * execute a get request by http client
	 * 
	 * @author MinhTDH
	 * @param request_link
	 * @return String
	 ********************************************************* 
	 */
	public static String excuteGetRequest(String request_link) {
		String ret = null;
		HttpGet get = new HttpGet(request_link);
		ret = executeHttpRequest(get);
		return ret;
	}

	/**
	 * execute a get request by http client
	 * 
	 * @author MinhTDH
	 * @param request
	 * @return String
	 ********************************************************* 
	 */
	public static String executeHttpRequest(HttpRequestBase request) {
		String ret = null;
		HttpClient client = getDefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			ret = client.execute(request, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	// post, get utilities use HttpUrlConnection
	/**
	 * @author MinhTDH
	 * @param http_link
	 *            : accept http link only
	 * @return HttpURLConnection
	 * @throws MalformedURLException
	 * @throws IOException
	 ********************************************************* 
	 */
	public static HttpURLConnection createDefaultURLConnect(String http_link)
			throws MalformedURLException, IOException {
		URL url = new URL(http_link);
		HttpURLConnection connection = createDefaultURLConnect(url);
		connection.setRequestProperty(HTTP.CONTENT_TYPE,
				"application/x-www-form-urlencoded");
		return connection;
	}

	/**
	 * 
	 * @author MinhTDH
	 * @param url
	 *            : accept url from http link only
	 * @return HttpURLConnection
	 * @throws IOException
	 ********************************************************* 
	 */
	public static HttpURLConnection createDefaultURLConnect(URL url)
			throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(TIME_OUT_CONNECTION);
		connection.setReadTimeout(TIME_OUT_SOCKET);
		disableConnectionReuseIfNecessary();
		return connection;
	}
	
	private static void disableConnectionReuseIfNecessary() {
		// Work around pre-Froyo bugs in HTTP connection reuse.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");

		}
	}
	
	/**
	 * object configure for post JSON data to server.
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @return HttpURLConnection
	 * @throws MalformedURLException
	 * @throws IOException
	 ********************************************************* 
	 */
	public static HttpURLConnection getJSONURLConnect(String http_link)
			throws MalformedURLException, IOException {
		URL url = new URL(http_link);
		HttpURLConnection connection = createDefaultURLConnect(url);
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty(HTTP.CONTENT_TYPE, "application/json");
		return connection;
	}

	/**
	 * read a InputStream data and convert to a String
	 * 
	 * @author MinhTDH
	 * @param in
	 * @return String
	 * @throws IOException
	 ********************************************************* 
	 */
	public static String readStream(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		String ret = null;
		StringBuilder bld = new StringBuilder();
		InputStream buffer_is = new BufferedInputStream(in);
		InputStreamReader reader = new InputStreamReader(buffer_is);
		BufferedReader buffer = new BufferedReader(reader);
		char[] bytes_buffer = new char[1024];
		int len = 0;
		while ((len = buffer.read(bytes_buffer)) != -1 ) {
			bld.append(bytes_buffer, 0, len);
		}
		buffer.close();
		ret = bld.toString();
		return ret;
	}

	/**
	 * build the post content string from a list argument
	 * 
	 * @author MinhTDH
	 * @param args
	 * @return String
	 ********************************************************* 
	 */
	public static String buildPostContent(List<? extends NameValuePair> args) {
		StringBuilder bld = new StringBuilder();
		for (NameValuePair pair : args) {
			if (!"".equals(pair.getName()) && pair.getName() != null) {
				bld.append(pair.getName()).append("=");
				if (pair.getValue() != null) {
					bld.append(pair.getValue());
				}
				bld.append("&");
			}
		}
		if (bld.length() > 0) {
			bld.deleteCharAt(bld.length() - 1);
		}
		return bld.toString();
	}

	/**
	 * get data by HttpUrlConnection, get Method
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @return String
	 ********************************************************* 
	 */
	public static String getData(String http_link) {
		String ret = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = createDefaultURLConnect(http_link);
			ret = readStream(urlConnection.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return ret;
	}

	/**
	 * post data with no argument
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @return String
	 ********************************************************* 
	 */
	public static String postBlankData(String http_link) {
		String ret = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = createDefaultURLConnect(http_link);
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			ret = readStream(urlConnection.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return ret;
	}

	/**
	 * post data to server via POST Method, use {@link HttpURLConnection}<br>
	 * use default configure
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @param args
	 * @return String
	 ********************************************************* 
	 */
	public static String postDataWithArgs(String http_link,
			List<? extends NameValuePair> args) {
		return postDataString(http_link, buildPostContent(args));
	}

	/**
	 * post a String data to link use HTTPURLConnection
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @param data
	 * @return String
	 ********************************************************* 
	 */
	public static String postDataString(String http_link, String data) {
		String ret = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = createDefaultURLConnect(http_link);
			ret = postDataString(urlConnection, data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return ret;
	}

	/**
	 * post a JSON data to link use HTTPURLConnection
	 * 
	 * @author MinhTDH
	 * @param http_link
	 * @param data
	 * @return String
	 ********************************************************* 
	 */
	public static String postDataJSONString(String http_link, String data) {
		String ret = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = getJSONURLConnect(http_link);
			ret = postDataString(urlConnection, data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return ret;
	}

	/**
	 * post data to server by POST method, use {@link HttpURLConnection}<br>
	 * don't disconnect after post and get data done<br>
	 * user must do the disconnect by himself
	 * 
	 * @author MinhTDH
	 * @param urlConnection
	 * @param data
	 * @return String
	 * @throws IOException
	 ********************************************************* 
	 */
	public static String postDataString(HttpURLConnection urlConnection,
			String data) throws IOException {
		String ret = null;
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoInput(true);
		if (data != null) {
			urlConnection.setDoOutput(true);
			byte[] data_bytes = data.getBytes(HTTP.UTF_8);
	
			urlConnection.setRequestProperty(HTTP.CONTENT_LEN,
					String.valueOf(data_bytes.length));
			urlConnection.setFixedLengthStreamingMode(data_bytes.length);
			OutputStream out = new BufferedOutputStream(
					urlConnection.getOutputStream());
			out.write(data_bytes);
			out.flush();
			out.close();
		}
		ret = readStream(urlConnection.getInputStream());
		return ret;
	}
	/**
	 * use for post mutil part Method
	 * @author MinhTDH
	 * @param http_link
	 * @param entity
	 * @return String
	 *********************************************************
	 */
	public static String postMultipartData(String http_link, MultipartEntity entity) {
		if (http_link == null || entity == null) {
			return null;
		}
		String ret = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = createDefaultURLConnect(http_link);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST"); 
			urlConnection.setRequestProperty("Content-Type", "multipart/form-data"); 
			urlConnection.setRequestProperty("charset", "utf-8");
			
			urlConnection.setRequestProperty("Content-Length", String
					.valueOf(entity.getContentLength()));
			urlConnection.setChunkedStreamingMode(0);
			
			OutputStream out = new BufferedOutputStream(
					urlConnection.getOutputStream());
			entity.writeTo(out);
			out.flush();
			out.close();
			
			InputStream in = urlConnection.getInputStream();//);
			ret = readStream(in);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO un comment if need read error stream
//			if (urlConnection != null) {
//				InputStream in = urlConnection.getErrorStream();
//				try {
//					ret = readStream(in);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return ret;
	}
//	public static String uploadCatalog(Bitmap cover_img, CatalogModel catalogModel,
//			String session_id, String catalog_id) {
//		String ret = null;
//		if (cover_img != null && catalogModel != null) {
//	    	String content = null;
//	    	ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
//			cover_img.compress(CompressFormat.JPEG, ICommonDefine.IMAGE_COMPRESS_QUALITY, bos);
//	    	byte[] data = bos.toByteArray();
//	    	if (content != null && data != null) {
//	    		StringBuilder file_name_send_to_server = 
//	    				new StringBuilder(ICommonDefine.FILE_NAME_SEND_TO_SERVER);
//	    		
//	    		file_name_send_to_server.append("_").append(catalog_id)
//	    				.append("_").append(TemplateUtil.getStringLongTimeFromNow())
//	    				.append(ICommonDefine.JPG_EXTENDSION);
//		    	ByteArrayBody bab = new ByteArrayBody(data, 
//		    			file_name_send_to_server.toString());
//		    	
//		    	MultipartEntity reqEntity = new MultipartEntity(
//		    							HttpMultipartMode.BROWSER_COMPATIBLE);
//		    	boolean isAddSuccess = true;
//		    	try {
//		    		reqEntity.addPart(ICommonDefine.SAVE_CATELOG_PARAM_CONTENT, 
//		    								new StringBody(content));
//		    	} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//					isAddSuccess = false;
//				}
//		    	reqEntity.addPart(ICommonDefine.SAVE_CATELOG_PARAM_FILE, bab);
////		    	reqEntity.addPart(ICommonDefine.SAVE_CATELOG_PARAM_FILE, fb);
//		    	if (isAddSuccess) {
//			    	Object[] args = { session_id, catalog_id};
//					String request_link = MessageFormat.format(ICommonDefine.SAVE_CATALOG_URL, args);
//			    	HttpPost httpPost = new HttpPost(request_link);
//			    	httpPost.addHeader("Content-Type", "multipart/form-data");
//			    	httpPost.setEntity( reqEntity);
//			    	
//			    	HttpClient client = getDefaultHttpClient();
//			    	
//			    	ret = executeHttpRequest(httpPost);
//		    	}
//	    	}
//		}
//		return ret;
//	}
}
