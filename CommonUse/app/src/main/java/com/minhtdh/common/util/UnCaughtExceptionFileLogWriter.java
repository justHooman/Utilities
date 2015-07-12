

package com.minhtdh.common.util;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnCaughtExceptionFileLogWriter implements UncaughtExceptionHandler {
	
	private UncaughtExceptionHandler oldHandle;
	private static String fileName;
	private UnCaughtExceptionFileLogWriter(UncaughtExceptionHandler handle) {
		oldHandle = handle;
	}
	public static void setUncaughtExceptionLogFile(String _fileName) {
		UnCaughtExceptionFileLogWriter uefw = 
				new UnCaughtExceptionFileLogWriter(Thread.getDefaultUncaughtExceptionHandler());
		if (_fileName != null && _fileName != "") {
			fileName = _fileName;
			
		}
		Thread.setDefaultUncaughtExceptionHandler(uefw);
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		File logFile = new File(Environment.getExternalStorageDirectory(), fileName);
		if (logFile.exists() == false) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
			StringBuilder bld = new StringBuilder();
			bld.append("time occur ").append(getStringLongTimeFromNow());
			pw.println(bld.toString());
			ex.printStackTrace(pw);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw!= null) {
				pw.flush();
				pw.close();
			}
		}
		if (oldHandle != null) {
			oldHandle.uncaughtException(thread, ex);
		}
	}
	public static String getStringLongTimeFromNow() {
		return getDateTimeFormat("yyyy:MM:dd HH:mm:ss");
	}
	/**
	 * get current date time and display as passing format
	 * @author MinhTDH
	 * @param format
	 * @return String
	 *********************************************************
	 */
	public static String getDateTimeFormat(String format) {
		String time="";
		Date date=new Date(System.currentTimeMillis());
		try {
			SimpleDateFormat SDF = new SimpleDateFormat(format, Locale.US);
			time = SDF.format(date);
		} catch (Exception e) {
		}
		return time;
	}
}
