package com.minhtdh.common;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.tinhvan.common.component.MyDialogFragment;
import com.tinhvan.common.util.ConversionUtil;

public class BaseFragmentActvity extends FragmentActivity {
	
	@Override
	protected void onResume() {
		super.onResume();
		isSavedState = false;
		if (need_dismiss_dlg) {
			need_dismiss_dlg = false;
			dismissFragmentDialog();
		}
		if (need_show_dialog) {
			need_show_dialog = false;
			reshowDialog(tempFragment);
			tempFragment = null;
		}
	}
	
	@Override
	protected void onStart() {
		isSavedState = false;
		super.onStart();
	}
	private boolean isSavedState = false;
	@Override
	protected void onPause() {
		isSavedState = true;
		super.onPause();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	/**
	 * set default action as back when click on btn_back in header
	 * @author MinhTDH 
	 *********************************************************
	 */
	public void onBackClick(View v) {
		super.onBackPressed();
	}
	@Override
	public void onBackPressed() {
		onBackClick(null);
	}
	private static final String FRAGMENT_TAG_DIALOG = "dialog";
	private boolean need_dismiss_dlg = false;
	private boolean need_show_dialog = false;
	private MyDialogFragment tempFragment;
	/**
	 * show dialog on screen, not cancelable
	 * @author MinhTDH
	 * @param _type 
	 *********************************************************
	 */
	protected void showFramentDialog(CharSequence msg) {
		showFramentDialogWithTask(msg, null, null);
	}
	
	/**
	 * show dialog on screen, not cancel able
	 * @author MinhTDH
	 * @param _type
	 * @param _cancel_listener 
	 *********************************************************
	 */
	protected void showFramentDialog(CharSequence msg, DialogInterface.OnCancelListener _cancel_listener) {

        showFramentDialogWithTask(msg, _cancel_listener, null);
	}

	protected void showFramentDialogWithTask(CharSequence msg,
			DialogInterface.OnCancelListener _cancel_listener,
			AsyncTask _task) {
		// DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DIALOG);
	    if (prev != null) {
	    	ft.remove(prev);
	    }
		// Create and show the dialog.
		MyDialogFragment newFragment = MyDialogFragment.newInstance(msg);
		if (_cancel_listener != null) {
			newFragment.setCancelable(true);
			newFragment.setOnCanceListener(_cancel_listener).setTask(_task);
		} else {
			newFragment.setCancelable(false);
		}
		need_dismiss_dlg = false;
		if (!isSavedState) {
			need_show_dialog = false;
			ft.addToBackStack(null);
			newFragment.show(ft, FRAGMENT_TAG_DIALOG);
		} else {
			need_show_dialog = true;
			tempFragment = newFragment;
		}
	}

	private void reshowDialog(MyDialogFragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DIALOG);
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		fragment.show(ft, FRAGMENT_TAG_DIALOG);
	}

	protected void dismissFragmentDialog() {
		DialogFragment prev = ConversionUtil.convertObject(getSupportFragmentManager()
						.findFragmentByTag("dialog"),
				DialogFragment.class);
	    if (prev != null) {
	    	if (!isSavedState) {
	    		need_show_dialog = false;
	    		prev.dismissAllowingStateLoss();
	    		need_dismiss_dlg = false;
	    	} else {
	    		need_dismiss_dlg = true;
	    	}
	    }
	}
	/**
	 * async class for show loading dialog when doinbackground
	 * @author MinhTDH
	 *
	 *********************************************************
	 */
	public abstract class LoadingTask extends AsyncTask<Void, Void, Object> {
		private OnCancelListener dialog_cancel;
		/**
		 * @param _lis: pass null if this task is not cancelable
		 */
		public LoadingTask(OnCancelListener _lis) {
			dialog_cancel = _lis;
		}
		/**
		 * set a default onCancelListener
		 * @param isCancelable
		 */
		public LoadingTask(boolean isCancelable) {
			if (isCancelable) {
				dialog_cancel = new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
					}
				};
			} else {
				dialog_cancel = null;
			}

		}
		/**
		 * override to display another message content
		 * @author MinhTDH
		 * @return CharSequence
		 *********************************************************
		 */
		protected CharSequence getMessage() {
			return getText(R.string.dlg_loading);
		}
		/**
		 * @author MinhTDH
		 * show waiting dialog
		 * @see android.os.AsyncTask#onPreExecute()
		 *********************************************************
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showFramentDialogWithTask(getMessage(),
					dialog_cancel, this);
		}
		/**
		 * @author MinhTDH
		 * dismiss waiting dialog
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 *********************************************************
		 */
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dismissFragmentDialog();
		}
	}
}
