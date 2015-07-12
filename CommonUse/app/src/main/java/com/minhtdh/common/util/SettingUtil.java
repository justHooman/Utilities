

package com.minhtdh.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;

public class SettingUtil {
//	/**
//	 * set screen timeout base on value in resource file
//	 * @author MinhTDH
//	 * @param context
//	 * @param timeoutIdx
//	 * @return boolean : return true if success
//	 *********************************************************
//	 */
//	public static boolean setScreenTimeout(Context context, int timeoutIdx) {
//		boolean ret = false;
//		// TODO need screen time out value
//		int[] values = context.getResources().getIntArray(R.array.setting_timeout_values);
//		if (values!= null && timeoutIdx >= 0 &&  timeoutIdx < values.length) {
//			ret = System.putInt(context.getContentResolver(), System.SCREEN_OFF_TIMEOUT, values[timeoutIdx]);
//		}
//		return ret;
//	}
//	/**
//	 * return index of time out value in setting_timeout_value; 
//	 * @author MinhTDH
//	 * @param context
//	 * @return int : return -1 if do not find match value
//	 *********************************************************
//	 */
//	public static int getScreenTimeout(Context context) {
//		int ret = -1;
//		// TODO need time out value
//		int[] values = context.getResources().getIntArray(R.array.setting_timeout_values);
//		if (values!= null) {
//			try {
//				int value = System.getInt(context.getContentResolver(), System.SCREEN_OFF_TIMEOUT);
//				for (int i = 0; i<values.length; i++) {
//					if (value == values[i]) {
//						ret = i;
//						break;
//					}
//				}
//			} catch (SettingNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		return ret;
//	}
	
	/**
	 * set sound mute base on isMute value
	 * @author MinhTDH
	 * @param context
	 * @param isMute 
	 *********************************************************
	 */
	public static void setSoundMute(Context context, boolean isMute) {
		if (context == null ) {
			return;
		}
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (am == null) {
			return;
		}
		
//		setStream(am, AudioManager.STREAM_ALARM, isMute);
//    	setStream(am, AudioManager.STREAM_DTMF, isMute);
//    	setStream(am, AudioManager.STREAM_MUSIC, isMute);
//    	setStream(am, AudioManager.STREAM_VOICE_CALL, isMute);
    	
    	if (isMute == true) {
    		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    	} else {
    		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    	}
	}
	
	/**
	 * set volume of audio stream = 0  or max value base on isMute value
	 * @author MinhTDH
	 * @param am
	 * @param streamIdx
	 * @param isMute : define stream volume is mute or not
	 *********************************************************
	 */
	private static void setStream(AudioManager am, int streamIdx, boolean isMute) {
    	int max = am.getStreamMaxVolume(streamIdx) / 2;
    	int volume = (isMute == true ? 0 : max); 
    	am.setStreamVolume(streamIdx, volume, AudioManager.FLAG_PLAY_SOUND);
    }
	
	/**
	 * check sound is mute or not
	 * @author MinhTDH
	 * @return boolean :  return true: if sound is mute
	 *********************************************************
	 */
	public static boolean isSoundMute(Context context) {
		boolean ret = true;
		if (context != null) {
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (am != null) {
				int mode = am.getRingerMode();
				if (mode == AudioManager.RINGER_MODE_NORMAL) {
					ret = false;
				} else {
					ret = true;
				}
			}
		}
		return ret;
	}
	
	/**
	 * set device auto rotation on/off.
	 * @author MinhTDH
	 * @param context
	 * @param isAutoRotate .true if you allow rotation,false if otherwise
	 * @return boolean : return true if success
	 *********************************************************
	 */
	public static boolean setDeviceAutoRotation(Context context, boolean isAutoRotate) {
		boolean ret = false;
		if (context != null) {
			//auto_rotate = 1; lock_auto_rotate = 0(base on app setting)
			int rotation = (isAutoRotate == true ? 1 : 0);
			ret = System
					.putInt(context.getContentResolver(), System.ACCELEROMETER_ROTATION, rotation);
		}
		return ret;
	}
	/**
	 * get current auto rotate of device
	 * @author MinhTDH
	 * @param context
	 * @return boolean : true if device auto rotation
	 *********************************************************
	 */
	public static boolean getDeviceAutoRotation(Context context) {
		boolean ret = true;
		try {
			//auto_rotate = 1; lock_auto_rotate = 0(base on app setting)
			int rotation = System
					.getInt(context.getContentResolver(), System.ACCELEROMETER_ROTATION);
			ret = (rotation == 1 ? true : false);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return ret; 
	}
	
	/**
	 * set activity orientation;
	 * NOTE THAT: activity could be recreate after this function;
	 * @author MinhTDH
	 * @param activity : activity need set orientation 
	 * @param isLock : pass true to lock rotate orientation to current orientation;
	 * pass false to set orientation base on isPortraitValue;
	 * @param isPortrait : pass true to lock orientation to portrait; 
	 * pass false to lock orientation to landscape;
	 * pass null to set orientation by sensor;
	 *********************************************************
	 */
	public static void setActivityOrientation(Activity activity, boolean isLock, Boolean isPortrait) {
		if (activity == null) {
			return;
		}
		if (isLock == true) {
			int orientation = activity.getResources().getConfiguration().orientation;
			int lock_orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			if (orientation != Configuration.ORIENTATION_PORTRAIT) {
				lock_orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			}
			activity.setRequestedOrientation(lock_orientation);
		} 
		else {
			int lock_orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
			if (isPortrait == Boolean.TRUE) {
				lock_orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			}
			else if (isPortrait == Boolean.FALSE) {
				lock_orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			}
			activity.setRequestedOrientation(lock_orientation);
		}
	}
	/**
	 * get current lock state of activity
	 * @author MinhTDH
	 * @param activity
	 * @return Boolean : null if current is not lock;
	 * true : if lock portrait;
	 * false : if lock landScape;
	 *********************************************************
	 */
	public static Boolean getActivityOrientation(Activity activity) {
		Boolean ret = null;
		int orientation = activity.getRequestedOrientation();
		switch (orientation) {
			case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT: {
				ret = Boolean.TRUE;
				break;
			}
			case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: {
				ret = Boolean.FALSE;
				break;
			}
			default: {
				break;
			}
		}
		return ret;
	}
}
