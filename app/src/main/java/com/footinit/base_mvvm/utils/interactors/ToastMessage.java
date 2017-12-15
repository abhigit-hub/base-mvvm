package com.footinit.base_mvvm.utils.interactors;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Created by Abhijit on 07-12-2017.
 */

public class ToastMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final ToastObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                if (s != null)
                    return;

                observer.onNewMessage(s);
            }
        });
    }


    public interface ToastObserver {
        /**
         * Called when there is a new message to be shown.
         * @param toastMessage The new message, non-null.
         */

        void onNewMessage(@StringRes Integer toastMessage);
    }
}