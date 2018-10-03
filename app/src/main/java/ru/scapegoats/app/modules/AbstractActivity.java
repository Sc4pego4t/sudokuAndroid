package ru.scapegoats.app.modules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.scapegoats.app.R;

public abstract class AbstractActivity<V> extends AppCompatActivity {

    protected V view;
    public Presenter presenter;
    protected Swipable swipable;

    protected abstract Presenter initPresenter();

    protected abstract V initView();

    protected abstract Swipable initSwipe();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = initView();
        presenter = initPresenter();
        presenter.onViewAttached(view);
        swipable = initSwipe();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransitionEnter();
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit(){
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }



    float x1, x2;
    float y1, y2;

    final int MIN_DISTANCE = 150;
    //detect swipes
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if(swipable!=null) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // Left to Right swipe action
                        if (x2 > x1) {
                            swipable.onRightSwipe();
                        }

                        // Right to left swipe action
                        else {
                            swipable.onLeftSwipe();
                        }
                    }

                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // Left to Right swipe action
                        if (y2 > y1) {
                            swipable.onDownSwipe();
                        }

                        // Right to left swipe action
                        else {

                            swipable.onUpSwipe();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
