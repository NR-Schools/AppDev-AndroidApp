package com.it193.dogadoptionapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.it193.dogadoptionapp.R;

public class NotificationUtility {

    public static void successAlert(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public static void errorAlert(Context ctx, String action, String message) {
        new AlertDialog.Builder(ctx)
                .setTitle(action)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setMessage(message + " Error")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

}
