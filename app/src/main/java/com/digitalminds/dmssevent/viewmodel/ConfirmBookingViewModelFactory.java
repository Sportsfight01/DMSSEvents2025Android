package com.digitalminds.dmssevent.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

public class ConfirmBookingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    Context context;

    public ConfirmBookingViewModelFactory(Context context) {
        this.context = context;
    }

    public ConfirmBookingViewModelFactory() {
        super();
    }
}
