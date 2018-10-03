package ru.scapegoats.app.activity.settings;

import ru.scapegoats.app.modules.Swipable;

public class SettingsSwipe implements Swipable {
    SettingsActivity activity;

    SettingsSwipe(SettingsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onLeftSwipe() {

    }

    @Override
    public void onRightSwipe() {
        activity.onBackPressed();
    }

    @Override
    public void onUpSwipe() {

    }

    @Override
    public void onDownSwipe() {

    }
}
