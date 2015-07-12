

package com.minhtdh.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * class provide function to process action
 * @author MinhTDH
 *
 *********************************************************
 */
public class ActionUtils {
	
	/**
	 * browse to a link from value get from view 
	 * @author MinhTDH
	 * @param context: current activity context
	 *********************************************************
	 */
	public static void actionBrowse(Context context, String value) {
		if (value != null && value.length() >0) {
			if (!value.startsWith("http://") && !value.startsWith("https://")) {
				value = "http://" + value;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
//			TemplateUtil.showToast(context, "browse");
			if (SystemUtil.isAvailable(context, intent)) {
				context.startActivity(intent);
			}
		}
	}
	/**
	 * call a number from value get from view
	 * require permission CALL_PHONE
	 * @author MinhTDH
	 * @param context: current activity context
	 *********************************************************
	 */
	public static void actionCall(Context context, String value) {
		if (value != null && value.length() >0) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, value);
			if (SystemUtil.isAvailable(context, callIntent)) {
				context.startActivity(callIntent);
			}
		}
	}
	/**
	 * SMS to a number from value get from view
	 * require permission SEND_SMS
	 * @author MinhTDH
	 * @param context: current activity context
	 *********************************************************
	 */
	public static void actionSMS(Context context, String value) {
		if (value != null && value.length() >0) {
			Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
					+ value));
//			TemplateUtil.showToast(context, "sms");
			if (SystemUtil.isAvailable(context, smsIntent)) {
				context.startActivity(smsIntent);
			}
		}
	}
	/**
	 * email to a mail from value get from view
	 * @author MinhTDH
	 * @param context: current activity context
	 *********************************************************
	 */
	public static void actionEmail(Context context, String value) {
		if (value != null && value.length() >0) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/html");
			emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] { value });
//			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//			emailIntent.putExtra(Intent.EXTRA_TEXT, "Content");
			if (SystemUtil.isAvailable(context, emailIntent)) {
				context.startActivity(emailIntent);
			}
		}
	}
//	/**
//	 * zoom a view;
//	 * @author MinhTDH
//	 *********************************************************
//	 */
//	public static void actionZoom(View v) {
//		if (v != null) {
//			Bitmap bm = null;
//			int w = 0;
//			int h = 0;
//			try  {
//				Rect rec = new Rect();
//				v.getLocalVisibleRect(rec);
//				w = (int) (rec.width() * ICommonDefine.ZOOM_RATIO);
//				h = (int) (rec.height() * ICommonDefine.ZOOM_RATIO);
//				if (v.getContext() instanceof Activity) {
//					Activity act = (Activity) v.getContext();
//					Point display_size = TemplateUtil.getDisplaySize(act);
//					w = (w < display_size.x ? w : display_size.x);
//					h = (h < display_size.y ? h : display_size.y);
//				}
//				bm = Bitmap.createBitmap(w, h, Config.ARGB_8888);
//				Canvas canvas = new Canvas(bm);
//				canvas.scale(((rec.width() == 0) ? 1 : w / (float) rec.width()), 
//								(rec.height() == 0) ? 1 : h / (float) rec.height());
//				v.draw(canvas);
//			} catch (OutOfMemoryError ome) {
//			}
//			if (bm != null) {
////				TemplateUtil.showToast(v.getContext(), "zoom " + w + " " + h);
//				Dialog dlg = new Dialog(v.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
//				View zoomView = new View(v.getContext());
//				zoomView.setBackgroundDrawable(
//						new BitmapDrawable(v.getResources(), bm));
//				dlg.setContentView(zoomView, new LayoutParams(w, h));
//				dlg.setCanceledOnTouchOutside(true);
//				dlg.getWindow().setGravity(Gravity.CENTER);
//				dlg.getWindow().setLayout(LayoutParams.WRAP_CONTENT, 
//						LayoutParams.WRAP_CONTENT);
//				dlg.show();
//			}
//		}
//	}
	
	
}
