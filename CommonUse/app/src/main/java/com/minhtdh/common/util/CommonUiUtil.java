package com.minhtdh.common.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class CommonUiUtil {
	public static final void setHtmlText(TextView tv, String source) {
		tv.setText((source == null ? null : Html.fromHtml(source)));
	}
	/**
	 * get and return screen size
	 * @author MinhTDH
	 * @param context
	 * @return Point
	 *********************************************************
	 */
	public static final Point getDisplaySize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point ret = new Point();
		ret.x = display.getWidth();
		ret.y = display.getHeight();
		return ret;
	}
	
	public static void hideSoftwareInput(Context context, View v) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(
                 Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    
    public static Point getDisplaySizeByPortrait(Context c) {
        Point ret = new Point();
        Point size = new Point();
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(size);
        Point fullSize = new Point();
        wm.getDefaultDisplay().getRealSize(fullSize);
        if (c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ret = size;
        } else {
            ret.x = fullSize.y;
            ret.y = fullSize.x - (fullSize.y - size.y);
        }
        return ret;
    }
}
