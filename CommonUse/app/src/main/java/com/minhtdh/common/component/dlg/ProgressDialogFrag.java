package com.minhtdh.common.component.dlg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.minhtdh.common.R;
import com.minhtdh.common.component.JacksonRequest;

/**
 * Created by minhtdh on 6/17/15.
 */
public class ProgressDialogFrag extends BaseDialog {

    public static ProgressDialogFrag newInstance(CharSequence msg) {
        ProgressDialogFrag pdf = new ProgressDialogFrag();
        pdf.mMsg = msg;
        return pdf;
    }

    private CharSequence mMsg;

    public CharSequence getMsg() {
        return mMsg;
    }

    public void setMsg(final CharSequence pMsg) {
        mMsg = pMsg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder pd = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.progress_layout, null, false);
        ((TextView) (v.findViewById(android.R.id.message))).setText(mMsg);
        pd.setView(v);
        pd.setCancelable(true);
        AlertDialog dlg = pd.create();
        customizeDlg(dlg);
        return dlg;
    }

    public static class ProgressRequestDlg<T> extends  ProgressDialogFrag implements Response
    .Listener<T>, Response.ErrorListener {

        private JacksonRequest.RequestWithDlg<T> mRequest;

        private Response
                .Listener<T> mResListener;
        private Response.ErrorListener mResErrListenner;

        public JacksonRequest.RequestWithDlg<T> getRequest() {
            return mRequest;
        }

        public void setRequest(final JacksonRequest.RequestWithDlg<T> pRequest) {
            mRequest = pRequest;
        }

        public Response.Listener<T> getResListener() {
            return mResListener;
        }

        public void setResListener(final Response.Listener<T> pResListener) {
            mResListener = pResListener;
        }

        public Response.ErrorListener getResErrListenner() {
            return mResErrListenner;
        }

        public void setResErrListenner(final Response.ErrorListener pResErrListenner) {
            mResErrListenner = pResErrListenner;
        }

        @Override
        public void onCancel(final DialogInterface dialog) {
            super.onCancel(dialog);
            if (mRequest != null) {
                mRequest.cancel();
            }
        }

        @Override
        public void onErrorResponse(final VolleyError error) {
            if (isResumed()) {
                dismiss();
            }
            if (mResErrListenner != null) {
                mResErrListenner.onErrorResponse(error);
            }
        }

        @Override
        public void onResponse(final T response) {
            if (isResumed()) {
                dismiss();
            }
            if (mResListener != null) {
                mResListener.onResponse(response);
            }
        }
    }
}
