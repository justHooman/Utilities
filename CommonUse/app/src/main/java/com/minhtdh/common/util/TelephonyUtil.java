/*
 * Copyright (C) 2013 TinhVan Outsourcing.
 */
package com.minhtdh.common.util;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author MinhTDH
 * Sep 24, 2013
 */
public final class TelephonyUtil {
    
    private static final Object getTelephony(Context c) throws ClassNotFoundException,
            NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        TelephonyManager telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = Class.forName(telephonyManager.getClass().getName());
        Method method = clazz.getDeclaredMethod("getITelephony");
        method.setAccessible(true);
        return method.invoke(telephonyManager);
    }
    
    public static final void endCall(Context c) {
        try {
            Object telephonyService = getTelephony(c);
            Method ans = telephonyService.getClass().getDeclaredMethod("endCall");
            ans.invoke(telephonyService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final void call(final Context c, String number) {
        try {
            Object telephonyService = getTelephony(c);
            Method ans = telephonyService.getClass().getDeclaredMethod("call", String.class);
            ans.invoke(telephonyService, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final void answerCall(final Context context) {
        long time = System.currentTimeMillis();
        Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
        headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        headSetUnPluggedintent.putExtra("state", 1);// 0 = unplugged  1 = Headset with microphone 2 = Headset without microphone
        headSetUnPluggedintent.putExtra("name", "Headset");
        try {
            context.sendOrderedBroadcast(headSetUnPluggedintent, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent event =  new KeyEvent(time, time, KeyEvent.ACTION_DOWN,
              KeyEvent.KEYCODE_HEADSETHOOK, 0);
            buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, event);
            context.sendOrderedBroadcast(buttonDown, null);
        } else { // froyo and beyond trigger on buttonUp instead of buttonDown
            Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent event =  new KeyEvent(time, time, KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_HEADSETHOOK, 0);
            buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, event);
            context.sendOrderedBroadcast(buttonUp, permission.CALL_PRIVILEGED);
        }
    }
    
    public static void waitTillPhoneOpTop(Context c) {
        long startTime = System.currentTimeMillis();
        boolean looping = true;
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        while (looping) {
            List<RunningTaskInfo> ls_rapi = am.getRunningTasks(1);
            String phoneName = "com.android.phone.InCallScreen";
            for (RunningTaskInfo task : ls_rapi) {
                String runningName =
                        (task == null || task.topActivity == null
                                || task.topActivity.flattenToString() == null
                                ? ""
                                : task.topActivity.flattenToString());
                if (runningName.contains(phoneName)) {
                    looping = false;
                    break;
                }
            }
            if (System.currentTimeMillis() - startTime > 3000) {
                break;
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * need {@link permission#MODIFY_AUDIO_SETTINGS}
     * @param c
     * @param isOn
     * @author MinhTDH
     */
    public final static boolean turnSpeaker(Context c, boolean isOn) {
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(isOn);
            return true;
        }
        return false;
    }
    
    public static boolean isSpeakerOn(Context c) {
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            return audioManager.isSpeakerphoneOn();
        }
        return false;
    }
    
    public static final boolean isPhoneCallAvail(Context c) {
        boolean ret = false;
        if (!SystemUtil.checkFeature(c, PackageManager.FEATURE_TELEPHONY)) {
            ToastUtil.showLongToast(c.getApplicationContext(), "R.string.device_no_telephony"/*R.string.device_no_telephony*/);
            return false;
        }
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
                ToastUtil.showLongToast(c.getApplicationContext(), "string.sim_not_avail"/*R.string.sim_not_avail*/);
                return false;
            }  
            if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                ToastUtil.showLongToast(c.getApplicationContext(), "R.string.phone_no_radio");
                return false;
            }
            // TODO error
//            int state = GreyHoundApplication.getCurrentCallServiceState(); 
//            if (state == ServiceState.STATE_OUT_OF_SERVICE || state == ServiceState.STATE_POWER_OFF) {
//                ToastUtil.showToast(c.getApplicationContext(), R.string.out_of_service);
//                return false;
//            }
            if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                ToastUtil.showLongToast(c.getApplicationContext(), "R.string.unable_to_detect_network_type");
                return false;
            }
            ret = true;
        }
        return ret;
    }
}
