package ru.scapegoats.app.activity.settings;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
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

        fillThemeContainer();

        settings = SettingsPreferences.getSettings(view.activity);

        view.animation.setChecked(getBool(settings.get(SettingsPreferences.ANIMATION)));
        view.areas.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTAREAS)));
        view.blocks.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTBLOCK)));
        view.mistakes.setChecked(getBool(settings.get(SettingsPreferences.HIGHLIGHTMISTAKES)));
        view.sound.setChecked(getBool(settings.get(SettingsPreferences.SOUND)));
        view.difficulty.setSelection(settings.get(SettingsPreferences.DIFFICULTY));
        view.difficulty.setOnItemSelectedListener(new DifficultyChangedListener());

        selectedTheme=settings.get(SettingsPreferences.THEME);
        frames.get(selectedTheme).setBackgroundColor(settingsView.activity
                .getResources().getColor(R.color.colorAccent));


    }

    int selectedTheme;
    private ArrayList<FrameLayout> frames;
    private void fillThemeContainer(){
        final Resources res=settingsView.activity.getResources();
        frames=new ArrayList<>();
        for (int i=0;i<4;i++) {
            final FrameLayout frame = new FrameLayout(settingsView.activity);

            ViewGroup.LayoutParams frameParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            frame.setLayoutParams(frameParams);

            TextView textView = new TextView(settingsView.activity);

            int size = Math.round(res.getDimension(R.dimen.ovalSize));
            FrameLayout.LayoutParams textViewParams = new FrameLayout.LayoutParams(size, size);

            switch (i){
                case 0: textView.setBackground(res.getDrawable(R.drawable.circle_lightblue));break;
                case 1: textView.setBackground(res.getDrawable(R.drawable.circle_dark));break;
                case 2: textView.setBackground(res.getDrawable(R.drawable.circle_pink));break;
                case 3: textView.setBackground(res.getDrawable(R.drawable.circle_orange));break;
            }
            int margin = Math.round(res.getDimension(R.dimen.margin05x));

            textViewParams.setMargins(margin,margin,margin,margin);

            textView.setLayoutParams(textViewParams);

            textView.setOnClickListener(new ThemeChangeListener(i));
            frame.addView(textView);
            frames.add(frame);
            settingsView.themeContainer.addView(frame);
        }
    }

    class ThemeChangeListener implements View.OnClickListener{
        int index;
        ThemeChangeListener(int index){
            this.index=index;

        }
        @Override
        public void onClick(View view) {
            selectedTheme=index;
            deleteFramesBackground();
            frames.get(index).setBackgroundColor(settingsView.activity
                    .getResources().getColor(R.color.colorAccent));
        }
    }

    private void deleteFramesBackground(){
        for(FrameLayout frame : frames){
            frame.setBackgroundColor(settingsView.activity
                    .getResources().getColor(android.R.color.transparent));
        }
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
        settings.put(SettingsPreferences.HIGHLIGHTBLOCK,getInt(settingsView.blocks.isChecked()));
        settings.put(SettingsPreferences.HIGHLIGHTMISTAKES,getInt(settingsView.mistakes.isChecked()));
        settings.put(SettingsPreferences.SOUND,getInt(settingsView.sound.isChecked()));
        settings.put(SettingsPreferences.ANIMATION,getInt(settingsView.animation.isChecked()));
        settings.put(SettingsPreferences.THEME,selectedTheme);
        SettingsPreferences.setSettings(settings,settingsView.activity);
    }
    @Override
    public void onDestroyed() {

    }
}
