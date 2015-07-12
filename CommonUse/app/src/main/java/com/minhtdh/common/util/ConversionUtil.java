package com.minhtdh.common.util;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class ConversionUtil {
	/**
	 * convert a object to a class , return null if class cast exception
	 * @author MinhTDH
	 * @param o
	 * @param returnClass
	 * @return T
	 *********************************************************
	 */
	public final static <T> T convertObject(Object o, Class<T> returnClass) {
		return ((returnClass != null)  && (returnClass.isInstance(o)) ? 
								returnClass.cast(o) : null);
	}
	/**
	 * convert a String to color value by add '#' character then parse
	 * @author MinhTDH
	 * @param colorStr
	 * @return int
	 *********************************************************
	 */
	public static int convertStringToColor(String colorStr) {
		int ret = 0;
		try {
			ret = Color.parseColor(new StringBuilder("").
					append(colorStr).toString());
		} catch (IllegalArgumentException iae) {
			
		}
		return ret;
	}
	/**
	 * get child view position of touch pos in parent group
	 * @author MinhTDH
	 * @param vg
	 * @param x
	 * @param y
	 * @return {@link AbsListView#INVALID_POSITION} if touch pos is not in any child
	 *********************************************************
	 */
	public final static int pointToPosition(final ViewGroup vg, final int x, final int y) {
		if (vg != null) {
			Rect frame = new Rect();
			final int count = vg.getChildCount();
	        for (int i = count - 1; i >= 0; i--) {
	            final View child = vg.getChildAt(i);
	            if (child.getVisibility() == View.VISIBLE) {
	                child.getHitRect(frame);
	                if (frame.contains(x, y)) {
	                    return i;
	                }
	            }
	        }
		}
        return AbsListView.INVALID_POSITION;
	}
	
	public static  float convertDipToPixel(int dp, Resources resources) {
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}
	
	public static final int TRUE_INT = 1;
	public static final int FALSE_INT = 0;
	public final static int convertBooleanToInt(boolean input) {
		return (input ? TRUE_INT : FALSE_INT);
	}
	
	public final static boolean convertIntToBoolean(int input) {
		return (input == TRUE_INT ? true : false);
	}
	
	public static String replaceEndStringByThreeDot(String oriString, int endPosition) {
        if (oriString.length() > 3 && endPosition < oriString.length()) {
            oriString = new StringBuilder(oriString).replace(endPosition - 2, endPosition + 1, "...")
                    .delete(endPosition + 1, oriString.length()).toString();
        }
        return oriString;
    }
}
