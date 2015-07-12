

package com.minhtdh.common.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

public class PhoneUtils {
	
	public static class SMSData {
		String number;
		String message;
		public SMSData(String _number, String _msg) {
			number = _number;
			message = _msg;
		}
	}
	
	public static interface OnSMSSentListener{
		/**
		 * case SMS sent success
		 * @author MinhTDH
		 * @param receiver
		 * @param context
		 * @param intent
		 *********************************************************
		 */
		void onSentSuccess(BroadcastReceiver receiver, Context context, Intent intent);
		/**
		 * case SMS sent fail
		 * @author MinhTDH
		 * @param receiver
		 * @param context
		 * @param intent
		 *********************************************************
		 */
		void onSentFail(BroadcastReceiver receiver, Context context, Intent intent);
	}
	
	public static interface OnSMSDeliveredListener{
		/**
		 * case SMS delivered success
		 * @author MinhTDH
		 * @param receiver
		 * @param context
		 * @param intent
		 *********************************************************
		 */
		void onDeliveredSuccess(BroadcastReceiver receiver, Context context, Intent intent);
		/**
		 * case SMS delivered fail
		 * @author MinhTDH
		 * @param receiver
		 * @param context
		 * @param intent
		 *********************************************************
		 */
		void onDeliveredFail(BroadcastReceiver receiver, Context context, Intent intent);
	}
	/**
	 * send SMS with data 
	 * @author MinhTDH
	 * @param current_context
	 * @param data
	 * @param sent_listener
	 * @param delivered_listener
	 *********************************************************
	 */
	public static void sendSMS(Context current_context, SMSData data,
			final OnSMSSentListener sent_listener, 
			final OnSMSDeliveredListener delivered_listener) {
		
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERD";
		
		// declare 2 intent use to broadcast message, to trigger SENT and DELIVERED event
		PendingIntent sentPI = PendingIntent.getBroadcast(current_context, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent
				.getBroadcast(current_context, 0, new Intent(DELIVERED), 0);
		
		// register SENT event handler 
		current_context.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// TODO show test sending success
					if (sent_listener != null) {
						sent_listener.onSentSuccess(this, context, null);
					}
					break;
				case Activity.RESULT_CANCELED:
					// TODO show test sending fail
					if (sent_listener != null) {
						sent_listener.onSentFail(this, context, null);
					}
					break;
				}
			}
		}, new IntentFilter(SENT));
		
		// register DELIVERED event handler 
		current_context.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						// TODO show test sent success
						if (delivered_listener != null) {
							delivered_listener.onDeliveredSuccess(this, context, intent);
						}
						break;
					case Activity.RESULT_CANCELED:
						// TODO show test sent fail
						if (delivered_listener != null) {
							delivered_listener.onDeliveredFail(this, context, intent);
						}
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		// create a SmsManager to send sms message
		SmsManager smsManager = SmsManager.getDefault();
		if (smsManager != null) {
			smsManager.sendTextMessage(data.number, null, data.message, sentPI, deliveredPI);
		}
	}
}
