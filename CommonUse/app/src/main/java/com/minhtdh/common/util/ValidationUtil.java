package com.minhtdh.common.util;

public class ValidationUtil {
	/**
	 * validate a email address
	 * @author MinhTDH
	 * @param input
	 * @return String :  return null if input is not valid
	 *********************************************************
	 */
	public static String validateEmail(String input) {
		String ret = null;
		if (input != null) {
			input = input.trim();
			if (input.length() > 0 && !input.contains(" ") && input.contains("@")) {
				int last_of_dot = input.lastIndexOf(".");
				if (input.indexOf("@") + 1 < last_of_dot && last_of_dot < input.length() - 1) {
					ret = input;
				}
			}
		}
		return ret;
	}
}
