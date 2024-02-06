package com.it193.dogadoptionapp.repository;

import android.content.Context;

import com.it193.dogadoptionapp.utils.AnimationUtility;

public abstract class RepositoryBase {

    protected Context ctx;
    protected ResponseCallback callback;

    public RepositoryBase(Context context) {
        AnimationUtility.getInstance().initialize(context);
        ctx = context;

        // Set default callback
        callback = (responseObject, errorMessage) -> {};
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }
}
