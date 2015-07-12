package com.minhtdh.common.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;


public class NotificationUtil {
	
	
	public static void generateNotification(Context context, int notify_id,
			Builder mBuilder, Intent resultIntent) {
		generateNotification(context, null, notify_id, mBuilder, resultIntent);
	}
	
	public static void generateNotification(Context context, String notify_tag,
			int notify_id, Builder mBuilder, Intent resultIntent) {
		Notification notification = createNotification(context, mBuilder, resultIntent);
		NotificationManager notifyManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.notify(notify_tag, notify_id, notification);
	}
	
	public static Notification createNotification(Context context, Builder mBuilder, Intent resultIntent) {
		if (resultIntent == null) {
			resultIntent = new Intent();
		}
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
					context,
					0,
					resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT
			);
		mBuilder.setContentIntent(resultPendingIntent);
		
		return mBuilder.build();
	}
	
}
