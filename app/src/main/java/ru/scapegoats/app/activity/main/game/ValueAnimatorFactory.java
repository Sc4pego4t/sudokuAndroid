package ru.scapegoats.app.activity.main.game;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

public class ValueAnimatorFactory {

    static public ValueAnimator getAnimator(int duration, int colorFrom, int colorTo) {
        ValueAnimator valueAnimator=ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }
}
