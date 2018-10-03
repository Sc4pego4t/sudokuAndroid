package ru.scapegoats.app.activity.main;

import android.content.Intent;

import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.activity.settings.SettingsActivity;
import ru.scapegoats.app.modules.Swipable;

public class MainSwipe implements Swipable {
    MainActivity activity;
    MainSwipe(MainActivity activity){
        this.activity=activity;
    }

    @Override
    public void onUpSwipe() {

    }

    @Override
    public void onDownSwipe() {

    }

    @Override
    public void onLeftSwipe() {
        activity.startActivityForResult(new Intent(activity,SettingsActivity.class),0);

    }

    @Override
    public void onRightSwipe() {
    }
}
