package com.minhtdh.common.util;

import android.Manifest.permission;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Utilities for download file from Internet <br>
 * require permission {@link permission#INTERNET} <br>
 * and {@link permission#WRITE_EXTERNAL_STORAGE}
 * @author MinhTDH
 *
 *********************************************************
 */
public class DownloadUtil {
	/**
	 * download file by app itself
	 * @author MinhTDH
	 * @param urlString
	 * @param path
	 * @return boolean
	 *********************************************************
	 */
	public static boolean downloadFile(String urlString, String path) {
		try{
	        //Open a connection to that URL.
	        URLConnection ucon = NetworkUtil.createDefaultURLConnect(urlString);
        
	        //Define InputStreams to read from the URLConnection.
	        // uses 3KB download buffer
	        InputStream is = ucon.getInputStream();
	        BufferedInputStream inStream = new BufferedInputStream(is, NetworkUtil.BUFFER_SIZE);
	        FileOutputStream outStream = new FileOutputStream(path);
	        byte[] buff = new byte[NetworkUtil.BUFFER_SIZE];
	
	        //Read bytes (and store them) until there is nothing more to read(-1)
	        int len;
	        while ((len = inStream.read(buff)) != -1) {
	            outStream.write(buff,0,len);
	        }
	        outStream.flush();
	        outStream.close();
	        inStream.close();
        }
		catch(Exception ex) {
        	return false;
        }
		return true;
	}
	
	/**
	 * download file by device {@link DownloadManager} ,<br>
	 * require <b>API 9</b> 
	 * @author MinhTDH
	 * @param context : context to start download 
	 * @param http_link : only accept HTTP link
	 * @param folder_path : destination folder
	 * @param file_name : downloaded file name, include extension
	 * @return long : download id for download file, unique in system
	 *********************************************************
	 */
	public static long downloadFileByManager(Context context, String http_link,
			String folder_path, String file_name) {
		
		Request request = generateDownloadRequest(http_link, folder_path, file_name);
		return downloadFileByManager(context, request);
	}
	/**
	 * download file by device {@link DownloadManager},<br>
	 * require <b>API 9</b> 
	 * @author MinhTDH
	 * @param context : context to start download
	 * @param request : request to download file
	 * @return long : download id for download file, unique in system
	 *********************************************************
	 */
	public static long downloadFileByManager(Context context, Request request) {
		DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		return dm.enqueue(request);
	}
	
	/**
	 * generate request to download file by device {@link DownloadManager},<br>
	 * require <b>API 9</b> <br>
	 * override this method to add more option to request download<br> 
	 * when use {@link #downloadFileByManager(Context, String, String, String)}
	 * @author MinhTDH
	 * @param http_link : only accept HTTP link
	 * @param folder_path : destination folder
	 * @param file_name : downloaded file name, include extension
	 * @return {@link Request}: the request for download file
	 *********************************************************
	 */
	public static Request generateDownloadRequest(String http_link,
			String folder_path, String file_name) {
		Uri download_uri = Uri.parse(http_link);
		File destionation_folder = new File(folder_path);
		destionation_folder.mkdirs();
		File destnation_file = new File(destionation_folder, file_name);
		Uri destination_uri = Uri.fromFile(destnation_file);
		
		DownloadManager.Request request = new Request(download_uri);
		request.setDestinationUri(destination_uri);
		request.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(file_name));
		return request;
	}
}
