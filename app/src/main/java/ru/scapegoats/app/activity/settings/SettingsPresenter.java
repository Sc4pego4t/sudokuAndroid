package ru.scapegoats.app.activity.settings;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import ru.scapegoats.app.R;
import ru.scapegoats.app.modules.Presenter;
import ru.scapegoats.app.modules.SettingsPreferences;

public class SettingsPresenter implements Presenter<SettingsView> {
    Map<String,Integer> settings;
    private SettingsView settingsView;
    int changedDifficulty;
    @Override
    public void onViewAttached(@NonNull SettingsView view) {
        settingsView=view;
        ActionBar actionBar=view.activity.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.settings);

        settings = SettingsPreferences.getSettings(view.activity);

        view.animation.setChecked(getBool(settings.get(SettingsPreferences.ANIMATION)));
        view.areas.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTAREAS)));
        view.bloks.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTBLOCK)));
        view.mistakes.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTMISTAKES)));
        view.sound.setChecked(getBool(settings.get(SettingsPreferences.SOUND)));

        view.difficulty.setSelection(settings.get(SettingsPreferences.DIFFICULTY));


        view.difficulty.setOnItemSelectedListener(new DifficultyChangedListener());
    }

    private class DifficultyChangedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            changedDifficulty=i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private boolean getBool(int num){
        return num != 0;
    }

    private int getInt(boolean b){
        if(b)
            return 1;
        else
            return 0;
    }



    @Override
    public void onViewDetached() {

    }

    void saveSettingsChanges(){
        settings.clear();
        //IT'S VITAL, PUT IT IN ORDER
        settings.put(SettingsPreferences.DIFFICULTY,settingsView.difficulty.getSelectedItemPosition());
        settings.put(SettingsPreferences.HIGHLIGHTAREAS,getInt(settingsView.areas.isChecked()));
        settings.put(SettingsPreferences.HIGHLIGHTBLOCK,getInt(settingsView.bloks.isChecked()));
        settings.put(SettingsPreferences.HIGHLIGHTMISTAKES,getInt(settingsView.mistakes.isChecked()));
        settings.put(SettingsPreferences.SOUND,getInt(settingsView.sound.isChecked()));
        settings.put(SettingsPreferences.ANIMATION,getInt(settingsView.animation.isChecked()));

        SettingsPreferences.setSettings(settings,settingsView.activity);
    }
    @Override
    public void onDestroyed() {

    }
}
