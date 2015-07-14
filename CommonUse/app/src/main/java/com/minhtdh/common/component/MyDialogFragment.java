package com.minhtdh.common.component;

import java.lang.ref.WeakReference;

import com.minhtdh.common.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyDialogFragment extends DialogFragment {
	public static final int DIALOG_LOADING = 0;
	CharSequence mMsg;
	private OnCancelListener mOnCanceListener;
	private WeakReference<AsyncTask> refTask;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static MyDialogFragment newInstance(CharSequence msg) {
        MyDialogFragment f = new MyDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putCharSequence("message", msg);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMsg = getArguments().getCharSequence("message");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
//        int style = DialogFragment.STYLE_NORMAL, theme = 0;
//        switch ((mNum-1)%6) {
//            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
//            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
//            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
//            case 4: style = DialogFragment.STYLE_NORMAL; break;
//            case 5: style = DialogFragment.STYLE_NORMAL; break;
//            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
//            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
//            case 8: style = DialogFragment.STYLE_NORMAL; break;
//        }
//        switch ((mNum-1)%6) {
//            case 4: theme = android.R.style.Theme_Holo; break;
//            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
//            case 6: theme = android.R.style.Theme_Holo_Light; break;
//            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
//            case 8: theme = android.R.style.Theme_Holo_Light; break;
//        }
        setStyle(style, theme);
    }
    
    public MyDialogFragment setOnCanceListener(OnCancelListener mCanceListener) {
		this.mOnCanceListener = mCanceListener;
		return this;
	}
    
    public MyDialogFragment setTask(AsyncTask _task) {
    	if (_task == null) {
    		refTask = null;
    	} else {
    		refTask = new WeakReference<AsyncTask>(_task);
    	}
    	return this;
    }
    
	@Override
    public void onCancel(DialogInterface dialog) {
    	super.onCancel(dialog);
    	if (refTask != null) {
    		AsyncTask task = refTask.get();
    		if (task != null) {
    			task.cancel(true);
    		}
    	}
    	if (mOnCanceListener != null) {
    		mOnCanceListener.onCancel(dialog);
    	}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_loading, container, false);
        View tv = v.findViewById(R.id.lbl_msg);
        ((TextView)tv).setText(mMsg);
		getDialog().setCanceledOnTouchOutside(false);
        return v;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	ProgressDialog ret = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    	ret.setMessage(mMsg);
    	ret.setCanceledOnTouchOutside(true);
    	return super.onCreateDialog(savedInstanceState);
    }
}
