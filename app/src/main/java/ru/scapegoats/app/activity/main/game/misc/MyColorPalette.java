package ru.scapegoats.app.activity.main.game.misc;

import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.modules.SettingsPreferences;

public class MyColorPalette {

    public int background;
    public int secondary;

    public int splitterThick;
    public int splitterThin;
    public int error;
    public int accent;

    public int highlight;
    public int select;

    public int textColor;

    public MyColorPalette(MainActivity activity, int theme){
        if(theme == SettingsPreferences.THEME_BLUE) {
            background = activity.getResources().getColor(R.color.background);
            secondary = activity.getResources().getColor(R.color.lightBlue);
            splitterThick = activity.getResources().getColor(R.color.darkGray);
            splitterThin = activity.getResources().getColor(R.color.gray);
            error = activity.getResources().getColor(R.color.errorColor);
            accent = activity.getResources().getColor(R.color.colorAccent);
            highlight = activity.getResources().getColor(R.color.blue);
            select = activity.getResources().getColor(R.color.darkBlue);
            textColor = activity.getResources().getColor(android.R.color.black);
        }

        if(theme == SettingsPreferences.THEME_DARK) {
            background = activity.getResources().getColor(R.color.dark);
            secondary = activity.getResources().getColor(R.color.black);
            splitterThick = activity.getResources().getColor(R.color.darkWhite);
            splitterThin = activity.getResources().getColor(R.color.white);
            error = activity.getResources().getColor(R.color.errorColor);
            accent = activity.getResources().getColor(R.color.yellow);
            highlight = activity.getResources().getColor(R.color.gray2);
            select = activity.getResources().getColor(R.color.gray3);
            textColor = activity.getResources().getColor(android.R.color.white);
        }

        if(theme == SettingsPreferences.THEME_PINK) {
            background = activity.getResources().getColor(R.color.background);
            secondary = activity.getResources().getColor(R.color.pink);
            splitterThick = activity.getResources().getColor(R.color.dark);
            splitterThin = activity.getResources().getColor(R.color.gray);
            error = activity.getResources().getColor(R.color.errorColor);
            accent = activity.getResources().getColor(R.color.colorAccent);
            highlight = activity.getResources().getColor(R.color.pink2);
            select = activity.getResources().getColor(R.color.pink3);
            textColor = activity.getResources().getColor(android.R.color.black);
        }

        if(theme == SettingsPreferences.THEME_ORANGE) {
            background = activity.getResources().getColor(R.color.background);
            secondary = activity.getResources().getColor(R.color.orange);
            splitterThick = activity.getResources().getColor(R.color.darkGray);
            splitterThin = activity.getResources().getColor(R.color.gray);
            error = activity.getResources().getColor(R.color.errorColor);
            accent = activity.getResources().getColor(R.color.colorAccent);
            highlight = activity.getResources().getColor(R.color.orange2);
            select = activity.getResources().getColor(R.color.orange3);
            textColor = activity.getResources().getColor(android.R.color.black);

        }

    }
}
