package com.minhtdh.common;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }
    String TAG = "TEST";
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View tmp;

        Log.d(TAG, new StringBuilder("onAttachedToWindow start").append(this).toString());
        tmp = findViewById(R.id.user_account_information);
        Log.d(TAG, new StringBuilder("onAttachedToWindow ").append(" tmp.getWidth()=")
                .append(tmp.getWidth()).append(this).toString());

        tmp = findViewById(R.id.v1);
        Log.d(TAG, new StringBuilder("onAttachedToWindow ").append(" tmp.getWidth()=")
                .append(tmp.getWidth()).append(this).toString());

        tmp = findViewById(R.id.v2);
        Log.d(TAG, new StringBuilder("onAttachedToWindow ").append(" tmp.getWidth()=")
                .append(tmp.getWidth()).append(this).toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
