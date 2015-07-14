package com.minhtdh.common.component;

import java.lang.ref.WeakReference;

import com.minhtdh.common.util.BitmapUtil;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DecodeTask extends AsyncTask<Void, Void, Drawable> {
	final WeakReference<ImageView> weak_ref_view;
	final WeakReference<ProgressBar> weak_ref_bar;
	final String path;
	final Point size = new Point();
	public DecodeTask(ImageView v, ProgressBar bar, String _path, int regW, int regH) {
		size.x = regW;
		size.y = regH;
		weak_ref_view = new WeakReference<ImageView>(v);
		weak_ref_bar = new WeakReference<ProgressBar>(bar);
		path = _path;
	}
	@Override
	protected Drawable doInBackground(Void... params) {
		Drawable ret = null;
		if ( weak_ref_view!= null) {
			View v = weak_ref_view.get();
			if (v != null) {
				ret = BitmapUtil.getDrawable(v.getContext(), 
							path, size.x, size.y);
			}
		}
		return ret;
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
		super.onPostExecute(result);
		if ( result != null && weak_ref_view != null) {
			ImageView iv = weak_ref_view.get();
			if (iv != null) {
				iv.setImageDrawable(result);
			}
		}
		if (weak_ref_bar != null) {
			ProgressBar bar = weak_ref_bar.get();
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
		}
	}
}
