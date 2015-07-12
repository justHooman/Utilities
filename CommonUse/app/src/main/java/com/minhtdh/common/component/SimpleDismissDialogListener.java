package com.minhtdh.common.component;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class SimpleDismissDialogListener implements OnClickListener {
	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}

}
