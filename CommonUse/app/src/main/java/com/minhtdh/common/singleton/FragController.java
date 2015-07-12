package com.minhtdh.common.singleton;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.minhtdh.common.R;

public class FragController {

    static class Holder {
        static FragController sInstance = new FragController();
    }

    public static FragController getIns() {
        return Holder.sInstance;
    }

    public Option generateDefaultOption(Fragment frag) {
        Option opt = new Option();
        opt.tag = frag.getClass().getSimpleName();
        opt.backStackName = frag.getClass().getName();
        return opt;
    }

    public static class Option {
        public static final int ACTION_REPLACE = 0;
        public static final int ACTION_ADD = 1;
        public int action = ACTION_REPLACE;
        public int placeHolder;
        public boolean addBackStack = true;
        
        public Option() {
            super();
        }
        
        public Option(int pPlaceHolder) {
            this();
            placeHolder = pPlaceHolder;
        }

//        public boolean finishCurrent = false;
        public boolean useAnimation = true;
        public String backStackName;
//        public boolean useSenderPlaceHolder = true;
        int placeHolderId;
        public String tag;

        public void setPlaceHolder(int holderId) {
//            useSenderPlaceHolder = false;
            placeHolderId = holderId;
        }
    }

    public void move(FragmentManager fm/*, Fragment oldFrag*/, Fragment frag, Option opt) {
        if (opt== null || frag == null) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        switch (opt.action) {
            case Option.ACTION_ADD :

                if (opt.placeHolderId == 0) {
                    ft.add(frag, opt.tag);
                } else {
                    ft.add(opt.placeHolderId, frag, opt.tag);
                }
                break;
            case Option.ACTION_REPLACE :
                if (opt.placeHolderId != 0) {
                    ft.replace(opt.placeHolder, frag, opt.tag);
                }
                break;
            default :
                break;
        }
        if (opt.addBackStack) {
            ft.addToBackStack(opt.backStackName);
        }
        ft.commit();
    }
}
