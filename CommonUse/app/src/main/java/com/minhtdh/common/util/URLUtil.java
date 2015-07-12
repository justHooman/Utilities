package com.minhtdh.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class URLUtil {
	public static final String UTF8Charset = "UTF-8";
	public static String encodeURLFormat(String input) {
		String ret = null;
		if (input != null) {
			try {
				ret = URLEncoder.encode(input, UTF8Charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public static String decodeURLFormat(String input) {
		String ret = null;
		if (input != null) {
			try {
				ret = URLDecoder.decode(input, UTF8Charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	/**
	 * use {@link MessageFormat} to add arg to url<br>
	 * encode each argument with @link {@link URLEncoder} <br>
	 * <b>maximum total length of String is 255 characters</b>
	 * @author MinhTDH
	 * @param input
	 * @param args
	 * @return String
	 *********************************************************
	 */
	public static String encodURLWithArgs(String input, Object... args) {
		String ret = null;
		if (args != null) {
			for (int i=0; i <args.length; i++) {
				args[i] = encodeURLFormat(args[i].toString());
			}
		}
		ret = MessageFormat.format(input, args);
		return ret;
	}
}
