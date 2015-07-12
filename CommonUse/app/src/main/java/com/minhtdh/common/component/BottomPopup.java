package com.minhtdh.common.component;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minhtdh.common.R;


public class BottomPopup extends PopupWindow implements OnClickListener {
	
	
	private LinearLayout ll_container;
	private TextView tv_title;
	private TextView btnCancel;
	private OnItemClickListener mListener; 
	
	public BottomPopup(Context context) {
		super(context);
		initDefaultContainer(context);
		setContentView(ll_container);
		setBackgroundDrawable(null);
//		setAnimationStyle(R.style.mileWindow);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
	}
	/**
	 * show the pop up at the bottom of the parent view
	 * @author MinhTDH
	 * @param parent
	 *********************************************************
	 */
	public void showAtBottom(View parent) {
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	
	private void initDefaultContainer(Context context) {
		ll_container = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.bottom_dialog_layout, null);
		tv_title = (TextView) ll_container.findViewById(R.id.txtTitle);
		btnCancel = (TextView) ll_container.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnCancel.setText(context.getString(android.R.string.cancel));
	}
	
	public void setOnItemClickListener(OnItemClickListener _lis) {
		mListener = _lis;
	}
	
	public void setTitle(CharSequence title) {
		tv_title.setText(title);
	}
	
	/**
	 * add new button to the menu
	 * @author MinhTDH
	 * @param btn_text
	 *********************************************************
	 */
	public void addButton(CharSequence btn_text) {
		TextView btn = (TextView) LayoutInflater.from(ll_container.getContext())
				.inflate(R.layout.default_option_button, ll_container, false);
		btn.setText(btn_text);
		btn.setOnClickListener(this);
		int childCount = ll_container.getChildCount();
		btn.setId(childCount - 2);
		ll_container.addView(btn, childCount - 1);
	}
	
	
	@Override
	public void onClick(View v) {
		if (btnCancel.equals(v)) {
			dismiss();
		} else {
			// this is use for position
			int id = v.getId();
			if (mListener != null) {
				mListener.onItemClick(this, v, id);
			}
		}
	}
	
	public static interface OnItemClickListener {
		void onItemClick(PopupWindow pw, View v, int position);
	}
	
}
