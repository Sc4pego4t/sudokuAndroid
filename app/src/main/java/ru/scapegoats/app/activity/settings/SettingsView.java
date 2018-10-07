package ru.scapegoats.app.activity.settings;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Map;

import androidx.appcompat.app.ActionBar;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.Viewable;

public class SettingsView implements Viewable {

    SettingsActivity activity;
    Switch areas,blocks,mistakes,animation;
    Spinner difficulty;

    LinearLayout themeContainer;

    SettingsView(SettingsActivity activity, View rootView){
        this.activity=activity;

        areas = rootView.findViewById(R.id.areas);
        blocks = rootView.findViewById(R.id.blocks);
        mistakes = rootView.findViewById(R.id.mistakes);
        difficulty = rootView.findViewById(R.id.difficulty);
        animation=rootView.findViewById(R.id.animation);

        themeContainer=rootView.findViewById(R.id.themeContainer);



    }

    @Override
    public void onAttach() {

    }
}
