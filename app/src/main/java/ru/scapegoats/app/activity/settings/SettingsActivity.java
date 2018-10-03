package ru.scapegoats.app.activity.settings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.scapegoats.app.R;
import ru.scapegoats.app.modules.AbstractActivity;
import ru.scapegoats.app.modules.Presenter;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.Swipable;
import ru.scapegoats.app.modules.dialogs.DialogDifficultyChanged;

public class SettingsActivity extends AbstractActivity<SettingsView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.settings_layout);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected Swipable initSwipe() {
        return new SettingsSwipe(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final int START_NEW_GAME=1;
    public static final int DO_NOT_START_NEW_GAME=0;
    public int startNewGameAfterDifficultyChanged=DO_NOT_START_NEW_GAME;



    @Override
    public void onBackPressed() {
        SettingsPresenter settingsPresenter=(SettingsPresenter)presenter;
        int previousDifficulty=settingsPresenter.settings.get(SettingsPreferences.DIFFICULTY);

        settingsPresenter.saveSettingsChanges();
        if(previousDifficulty!=settingsPresenter.changedDifficulty){
            DialogDifficultyChanged dialogDifficultyChanged=new DialogDifficultyChanged(this);
            dialogDifficultyChanged.getDialog().show();
        } else {
            setResult(startNewGameAfterDifficultyChanged);
            super.onBackPressed();
        }
    }


    @NonNull
    @Override
    protected Presenter initPresenter() {
        return new SettingsPresenter();
    }

    @NonNull
    @Override
    protected SettingsView initView() {
        return new SettingsView(this,findViewById(android.R.id.content));
    }

}
