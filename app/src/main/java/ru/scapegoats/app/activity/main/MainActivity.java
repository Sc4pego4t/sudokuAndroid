package ru.scapegoats.app.activity.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.game.misc.CoupleInt;
import ru.scapegoats.app.activity.settings.SettingsActivity;
import ru.scapegoats.app.modules.AbstractActivity;
import ru.scapegoats.app.modules.Presenter;
import ru.scapegoats.app.modules.Swipable;
import ru.scapegoats.app.modules.dialogs.AppRater;

public class MainActivity extends AbstractActivity<MainView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.main_layout);
        super.onCreate(savedInstanceState);
        AppRater.appLaunched(this);
        int l;
        int k;
        long llasd;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        ((MainPresenter)presenter).optionItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected Presenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void onResume() {
        ((MainPresenter)presenter).loadSettings();
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == SettingsActivity.START_NEW_GAME){
            ((MainPresenter)presenter).startNewGame();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @NonNull
    @Override
    protected MainView initView() {
        return new MainView(this,findViewById(android.R.id.content));
    }

    @NonNull
    @Override
    protected Swipable initSwipe() {
        return new MainSwipe(this);
    }

}
