package com.minhtdh.common.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    
    public static void showLongToast(Context context, int content_id) {
        showToast(context, context.getString(content_id), Toast.LENGTH_LONG);
    }
    
    public static void showLongToast(Context context, String content) {
        showToast(context, content, Toast.LENGTH_LONG);
    }
    
    public static void showToast(Context context, int content_id) {
        showToast(context, context.getString(content_id));
    }
    
    public static void showToast(Context context, String content) {
        showToast(context, content, Toast.LENGTH_SHORT);
    }
    
    public static void showToast(Context context, String content, int duration) {
		Toast toast = Toast.makeText(context, content, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
