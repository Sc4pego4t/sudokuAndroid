package ru.scapegoats.app.modules;

import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
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

    public static final int CHECKED=1;
    public static final int UNCHECKED=0;


    public static final ArrayList<String> SETTINGS_LIST=new ArrayList<String>(){{
        add(DIFFICULTY);
        add(HIGHLIGHTAREAS);
        add(HIGHLIGHTBLOCK);
        add(HIGHLIGHTMISTAKES);
        add(SOUND);
        add(ANIMATION);
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
    public static Map<String,Integer> getSettings(AbstractActivity activity){
        Map<String,Integer> settingsMap=new HashMap<>();
        SharedPreferences settingsPrefs=activity.getSharedPreferences(PREFERENCE_SETTINGS_NAME,0);
        for(String setting: SETTINGS_LIST){
            int storedInt=settingsPrefs.getInt(setting,-1);
            //if it is our first initialization after installing app
            if(storedInt==EMPTY_SETTING){
                switch (setting){
                    case DIFFICULTY: storedInt=DIFFICULTY_BEGINNER; break;
                    case HIGHLIGHTBLOCK: storedInt=UNCHECKED; break;
                    default: storedInt=CHECKED; break;
                }
            }

            settingsMap.put(setting,storedInt);
        }

        return settingsMap;
    }
}
