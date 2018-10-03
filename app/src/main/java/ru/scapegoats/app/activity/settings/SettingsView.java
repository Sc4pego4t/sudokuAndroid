package ru.scapegoats.app.activity.settings;

import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Map;

import androidx.appcompat.app.ActionBar;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.Viewable;

public class SettingsView implements Viewable {

    SettingsActivity activity;
    Switch areas,bloks,sound,mistakes;
    Spinner difficulty;
    SettingsView(SettingsActivity activity, View rootView){
        this.activity=activity;

        areas = rootView.findViewById(R.id.areas);
        bloks = rootView.findViewById(R.id.blocks);
        mistakes = rootView.findViewById(R.id.mistakes);
        sound = rootView.findViewById(R.id.sound);
        difficulty = rootView.findViewById(R.id.difficulty);

    }

    @Override
    public void onAttach() {

    }
}
