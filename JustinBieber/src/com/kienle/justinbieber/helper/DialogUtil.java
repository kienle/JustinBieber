package com.kienle.justinbieber.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kienle.justinbieber.R;

public class DialogUtil {

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
    
    public static Dialog createConfirmExistDialog(Context context,
            DialogInterface.OnClickListener onClickListenner,
            int messageId) {
        return new AlertDialog.Builder(context).setMessage(messageId)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, onClickListenner)
                .setNegativeButton(R.string.no, null).show();
    }
}
