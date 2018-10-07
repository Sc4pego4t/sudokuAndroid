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


    public enum Settings{
        Difficulty("dif"),
        HighlightAreas("areas"),
        HighlightBlocks("blocks"),
        HighlightMistakes("mistakes"),
        Animation("animation"),
        Theme("theme");

        public String name;
        Settings(String name){
            this.name=name;
        }
    }
    public enum Difficulties{
        Beginner(0),
        Intermediate(1),
        Professional(2);
        public int index;
        Difficulties(int index){
            this.index=index;
        }
    }

    public enum Themes{
        Blue(0),
        Dark(1),
        Pink(2),
        Orange(3);
        public int index;
        Themes(int index){
            this.index=index;
        }
        static public Themes valueOf(int index){
            switch (index){
                case 0:return Blue;
                case 1:return Dark;
                case 2:return Pink;
                case 3:return Orange;

                default:return null;
            }
        }
    }
    public enum States{
        Checked(1),
        Unchecked(0);
        public int index;
        States(int index){
            this.index=index;
        }
    }


    public static void setSettings(@NonNull EnumMap<Settings,Integer> newSettings, AbstractActivity activity){
        SharedPreferences.Editor editor=activity.getSharedPreferences(PREFERENCE_SETTINGS_NAME,0).edit();
        for (Settings setting:Settings.values()) {
            try {
                editor.putInt(setting.name, newSettings.get(setting));
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
            //if it is our first initialization after installing app
            if(storedInt==EMPTY_SETTING){
                switch (setting){
                    case Difficulty: storedInt=Difficulties.Beginner.index; break;
                    case Theme: storedInt=Themes.Blue.index; break;

                    default: storedInt = States.Checked.index; break;
                }
            }

            settingsMap.put(setting,storedInt);
        }

        return settingsMap;
    }
}
