

package com.minhtdh.common.component;

import android.os.Handler;

public abstract class HandlerThreadAsyncTask<Params, Progress, Result> {
	private final int DO_POST_EXCUTE = 10001;
	private final int UPDATE_PROGRESS_CODE = 10002;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				if (msg.what == DO_POST_EXCUTE) {						
					onPostExecute((Result) msg.obj);
				} else if (msg.what == UPDATE_PROGRESS_CODE) {
					onProgressUpdate((Progress[]) msg.obj);
				}
			}
		};
	};
	private Thread mThread;
	private Params[] mParam;
	public void execute(Params... param) {
		this.mParam = param;
		onPreExecute();
		try {
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (Thread.interrupted()) {
						return;
					}
					Result ret = doInBackGround(mParam);
					if (Thread.interrupted()) {
						return;
					}
					mHandler.obtainMessage(DO_POST_EXCUTE, ret).sendToTarget();
				}
			});
			mThread.start();
		} catch (IllegalStateException ise) {
			throw new RuntimeException("task can only start once time");
		}
	}
	private boolean cancel = false;
	
	public boolean isCancel() {
		return cancel;
	}

	public void cancel() {
		cancel = true;
		if (mThread != null && mThread.isAlive() == true) {
			try {
				mThread.interrupt();
			} catch (Exception e) {
			}
		}
		onCancel();
	}
	
	protected void publishProgress(Progress... value) {
		mHandler.obtainMessage(UPDATE_PROGRESS_CODE, value).sendToTarget();
	}
	
	protected void onPreExecute() {
	}
	protected abstract Result doInBackGround(Params... param);
	protected void onPostExecute(Result result) {
	}
	protected void onProgressUpdate(Progress... value) {
	}
	protected void onCancel() {
	}
}
