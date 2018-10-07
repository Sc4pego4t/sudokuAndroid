package ru.scapegoats.app.modules;

import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class SettingsPreferences {
    private static final String PREFERENCE_SETTINGS_NAME="settings_preferences";
    private static final int EMPTY_SETTING=-1;

    public static final String DIFFICULTY="dif";

    public static final int DIFFICULTY_BEGINNER=0;
    public static final int DIFFICULTY_INTERMIDIATE=1;
    public static final int DIFFICULTY_PROFESSIONAL=2;

    public static final String HIGHLIGHTAREAS="areas";
    public static final String HIGHLIGHTBLOCK="blocks";
    public static final String HIGHLIGHTMISTAKES="mistakes";
    public static final String SOUND="sound";
    public static final String ANIMATION="animation";

    public enum Settings{
        Difficulty("dif"),
        HighlightAreas("areas"),
        HighlightBlocks("blocks"),
        HighlightMistakes("mistakes"),
        Sound("sound"),
        Animation("animation"),
        Theme("theme");

        String name;
        Settings(String name){
            this.name=name;
        }

        public enum Difficulties{
            Beginner(0),
            Intermediate(1),
            Professional(2);
            int index;
            Difficulties(int index){
                this.index=index;
            }
        }
        public enum Themes{
            Blue(0),
            Dark(1),
            Pink(2),
            Orange(3);
            int index;
            Themes(int index){
                this.index=index;
            }
        }
        public enum States{
            Checked(0),
            Unchecked(1);
            int index;
            States(int index){
                this.index=index;
            }
        }
    }

    public static final int CHECKED=1;
    public static final int UNCHECKED=0;

    public static final String THEME="theme";

    public static final int THEME_BLUE=0;
    public static final int THEME_DARK=1;
    public static final int THEME_PINK=2;
    public static final int THEME_ORANGE=3;




    public static final ArrayList<String> SETTINGS_LIST=new ArrayList<String>(){{
        add(DIFFICULTY);
        add(HIGHLIGHTAREAS);
        add(HIGHLIGHTBLOCK);
        add(HIGHLIGHTMISTAKES);
        add(SOUND);
        add(ANIMATION);
        add(THEME);
    }};


    public static void setSettings(@NonNull Map<String,Integer> newSettings, AbstractActivity activity){
        SharedPreferences.Editor editor=activity.getSharedPreferences(PREFERENCE_SETTINGS_NAME,0).edit();
        for (String setting:SETTINGS_LIST) {
            try {
                editor.putInt(setting, newSettings.get(setting));
            } catch (Exception e){
                Log.e("ER",e.getMessage());
            }
        }
        editor.apply();
    }
    @NonNull
    public static EnumMap<Settings,Integer> getSettings(AbstractActivity activity){
        EnumMap<Settings,Integer> settingsMap=new EnumMap<>(Settings.class);

        SharedPreferences settingsPrefs=activity.getSharedPreferences(PREFERENCE_SETTINGS_NAME,0);

        for(Settings setting: Settings.values()){
            int storedInt=settingsPrefs.getInt(setting.name,-1);
            Log.e("Sett",setting.name);
            //if it is our first initialization after installing app
            if(storedInt==EMPTY_SETTING){
                switch (setting){
                    case Difficulty: storedInt=Settings.Difficulties.Beginner.index; break;
                    case Theme: storedInt=Settings.Themes.Blue.index; break;

                    default: storedInt= Settings.States.Checked.index; break;
                }
            }

            settingsMap.put(setting,storedInt);
        }

        return settingsMap;
    }
}
