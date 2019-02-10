package com.example.android.bakingapp.idlingResource;

import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.test.espresso.IdlingResource;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile IdlingResource.ResourceCallback resourceCallback;

    /* Idleness is controlled with this boolean. */
    private final AtomicBoolean isIdleNow = new AtomicBoolean(true);

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

    /**
     * Set the new idle state; if isIdleNow is true, it pings the resourceCallback.
     * Boolean isIdleNow is false if there are pending operations; if not, it is true
     */
    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && resourceCallback != null) {
            Objects.requireNonNull(resourceCallback).onTransitionToIdle();
        }
    }
}
