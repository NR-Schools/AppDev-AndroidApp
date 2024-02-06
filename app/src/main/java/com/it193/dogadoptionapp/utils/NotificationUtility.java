package com.it193.dogadoptionapp.utils;

import android.content.Context;
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
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public static void infoAlert(Context ctx, String message) {
        new AlertDialog.Builder(ctx)
                .setTitle("Information Alert")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
