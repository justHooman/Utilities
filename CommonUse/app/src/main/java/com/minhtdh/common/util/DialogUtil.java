package com.minhtdh.common.util;

import com.minhtdh.common.component.SimpleDismissDialogListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * class provide function to process action
 * 
 * @author MinhTDH
 * 
 ********************************************************* 
 */
public class DialogUtil {
	/**
	 * show alert dialog with only one button
	 * @author MinhTDH
	 * @param context
	 * @param title
	 * @param message
	 * @param buttonText
	 * @param _positive_listener
	 *********************************************************
	 */
	public static Dialog showAlertWithPositiveButton(final Context context,
			String title, String message,
			String buttonText, DialogInterface.OnClickListener _positive_listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(buttonText, _positive_listener);
		return builder.show();
	}
	/**
	 * show dialog 1 button default dismiss
	 * @author MinhTDH
	 * @param context
	 * @param title
	 * @param message
	 * @param buttonText void
	 *********************************************************
	 */
	public static void showAlertWithPositiveButton(final Context context,
			String title, String message,
			String buttonText) {
		showAlertWithPositiveButton(context, title, message, buttonText, 
				new SimpleDismissDialogListener());
	}
	
	/**
	 * Show alert dialog with 2 button
	 * @author MinhTDH
	 * @param context
	 * @param title
	 * @param message
	 * @param positiveButtonText
	 * @param _positive_listener
	 * @param negativeButtonText
	 * @param _negative_listener
	 *********************************************************
	 */
	public static void showAlertWithTwoButton(final Context context,
			String title, String message,
			String positiveButtonText, DialogInterface.OnClickListener _positive_listener,
			String negativeButtonText, DialogInterface.OnClickListener _negative_listener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveButtonText, _positive_listener);
		builder.setNegativeButton(negativeButtonText, _negative_listener);
		builder.show();
	}
	/**
	 * show dialog 2 button, negative default dismiss.
	 * @author MinhTDH
	 * @param context
	 * @param title
	 * @param message
	 * @param positiveButtonText
	 * @param _positive_listener
	 * @param negativeButtonText
	 *********************************************************
	 */
	public static void showAlertWithTwoButton(final Context context,
			String title, String message,
			String positiveButtonText, DialogInterface.OnClickListener _positive_listener,
			String negativeButtonText) {
		showAlertWithTwoButton(context, title, message, 
				positiveButtonText, _positive_listener, 
				negativeButtonText, new SimpleDismissDialogListener());
	}
	
//	public static void showDefaultErrorDialog(Context context, String msg_error) {
//		DialogUtil.showAlertWithPositiveButton(context, 
//				context.getString(R.string.dlg_error_title), 
//				(msg_error != null ? msg_error :
//					context.getString(R.string.msg_response_error_default)), 
//				context.getString(R.string.btn_ok), new SimpleDismissDialogListener() );
//	}
//	/**
//	 * show default error dialog when fail to get response from sever
//	 * @author MinhTDH
//	 * @param context
//	 *********************************************************
//	 */
//	public static void showDefaultErrorDialog(Context context) {
//		DialogUtil.showDefaultErrorDialog(context, null);
//	}
//	/**
//	 * show dialog for the server result
//	 * @author MinhTDH
//	 * @param context
//	 * @param result
//	 *********************************************************
//	 */
//	public static boolean showErrorDialogForServerResult(Context context, ServerResultModel result) {
//		boolean ret = false;
//		if (result == null) {
//			DialogUtil.showDefaultErrorDialog(context);
//			ret = true;
//		} else {
//			if (!result.isResult()) {
//				DialogUtil.showDefaultErrorDialog(context, result.getErrorDesc());
//				ret = true;
//			}
//		}
//		return ret;
//	}
}
