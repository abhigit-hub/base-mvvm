package com.footinit.base_mvvm.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


public final class ViewAnimationUtils {

    private ViewAnimationUtils() {
        // This class is not publicly instantiable
    }

    public static void scaleAnimateView(View view) {
        ScaleAnimation animation =
                new ScaleAnimation(
                        1.15f, 1, 1.15f, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

        view.setAnimation(animation);
        animation.setDuration(100);
        animation.start();
    }

}
