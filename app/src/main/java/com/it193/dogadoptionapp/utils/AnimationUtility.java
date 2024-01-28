package com.it193.dogadoptionapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.it193.dogadoptionapp.R;

/**
 * This is only for loading screen and other types of animation.
 */
public class AnimationUtility {

    private static AnimationUtility s_instance;
    private AnimationUtility() {
    }
    public static AnimationUtility getInstance() {
        if (s_instance == null)
            s_instance = new AnimationUtility();
        return s_instance;
    }

    private AlertDialog loadingAnimDialog;

    public void initialize(Context ctx) {
        // Initialize AlertDialog Loading Animation
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setView(LayoutInflater.from(ctx).inflate(R.layout.app_loading_animation, null));
        loadingAnimDialog = alert.create();
        loadingAnimDialog.getWindow().setBackgroundDrawable(
                new android.graphics.drawable.ColorDrawable(Color.BLACK)
        );
        loadingAnimDialog.setCancelable(false);
    }

    public void startLoading() {
        loadingAnimDialog.show();
    }

    public void endLoading() {
        loadingAnimDialog.dismiss();
    }
}
