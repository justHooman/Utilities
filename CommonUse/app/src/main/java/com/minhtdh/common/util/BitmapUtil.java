

package com.minhtdh.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class BitmapUtil {

	/**
	 * calculate the sample size for decode just need size bitmap
	 * @author MinhTDH
	 * @param options
	 * @param reqWidth 
	 * @param reqHeight
	 * @return int  
	 *********************************************************
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int inSampleSizeH = (int) FloatMath.ceil(height / (float) reqHeight);
			int inSampleSizeW = (int) FloatMath.ceil(width / (float) reqWidth);
			inSampleSize = (inSampleSizeH > inSampleSizeW ? inSampleSizeH : inSampleSizeW);
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampleBitmapFromFile(String fileName, final int regWidth, final int regHeight) {
		Bitmap ret = null;
		if (regWidth > 0  && regHeight > 0 && fileName != null) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, opts);
			opts.inJustDecodeBounds = false;
			try {
			    ret = BitmapFactory.decodeFile(fileName, opts);
			} catch (OutOfMemoryError e) {
			    e.printStackTrace();
			}
		}
		return ret;
	}

	public static Bitmap decodeSampleBitmapDirectFromUrl(String url, final int regWidth, final int regHeight) {
		Bitmap ret = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			connection = NetworkUtil.createDefaultURLConnect(url);
			connection.connect();
			is = connection.getInputStream();
			BitmapFactory.decodeStream(is, null, options);
			is.close();
			connection.disconnect();
			options.inSampleSize = calculateInSampleSize(options, regWidth, regHeight);
			
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			connection = NetworkUtil.createDefaultURLConnect(url);
			connection.connect();
			is = connection.getInputStream();
			ret = BitmapFactory.decodeStream(is, null, options);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is!= null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null ) {
				connection.disconnect();
			}
		}
		return ret;
	}
	public static Drawable getDrawable(Context context,
			String pathName, final int regWidth, final int regHeight) {
		Drawable ret = null;
		Bitmap bm = decodeSampleBitmapDirectFromUrl(pathName,
							regWidth, regHeight);
		if (bm != null) {
			ret = new BitmapDrawable(context.getResources(), bm);
		}
		return ret;
	}
}
