package com.minhtdh.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.List;

public class SystemUtil {

	
	/**
  	 * check if system can handle for match a intent or not
  	 * @author MinhTDH
  	 * @param ctx
  	 * @param intent
  	 * @return boolean
  	 *********************************************************
  	 */
	public static boolean isAvailable(Context ctx, Intent intent) {
		final PackageManager mgr = ctx.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
  	/**
  	 * check if running version is later than input version
  	 * @author MinhTDH
  	 * @param minimum_version : a version code value in {@link Build.VERSION_CODES}
  	 * @return boolean
  	 *********************************************************
  	 */
	public static boolean checkBuildVersion(int minimum_version) {
		return (Build.VERSION.SDK_INT > minimum_version ? true : false);
	}

  	/**
  	 * check if system have feature such as camera, bluetooth, ...
  	 * @author MinhTDH
  	 * @param context
  	 * @param feature : one of the features in {@link PackageManager}
  	 * @return boolean
  	 *********************************************************
  	 */
  	public static boolean checkFeature(Context context, String feature) {
  		boolean ret = false;
  		if (context != null && context.getPackageManager() != null) {
  			ret = context.getPackageManager().hasSystemFeature(feature);
  		}
  		return ret;
  	}
  	/**
  	 * start intent for launch app that can open by Broadcast Intent
  	 * @param context
  	 * @param intent
  	 * @return
  	 */
    public static Intent getIntentForLaunch(Context context, final Intent intent) {
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> activities =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (activities != null && !activities.isEmpty()) {
            return getIntentForLaunch(context, activities.get(0).activityInfo.packageName);
        }
        return null;
    }

    public static Intent getIntentForLaunch(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }
    /**
     * This method is used to get real path of file from from uri
     * 
     * @param contentUri
     * @return String
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (contentUri == null) {
            return null;
        }
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            ContentResolver cr = context.getContentResolver();
            if (cr != null) {
                Cursor cursor = cr.query(contentUri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String ret = cursor.getString(column_index);
                cursor.close();
                return ret;
            } else {
                return contentUri.getPath();
            }
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }
}
