package ru.scapegoats.app.modules;

import android.view.View;

public interface Presenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
