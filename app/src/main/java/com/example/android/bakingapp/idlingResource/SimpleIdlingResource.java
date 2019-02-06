package com.example.android.bakingapp.idlingResource;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.test.espresso.IdlingResource;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile IdlingResource.ResourceCallback resourceCallback;

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
