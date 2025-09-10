package com.digitalminds.dmssevent.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChoosePlayerViewMOdelFactory extends ViewModelProvider.NewInstanceFactory {
    Context context;

    public ChoosePlayerViewMOdelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return super.create(modelClass);
    }
}
