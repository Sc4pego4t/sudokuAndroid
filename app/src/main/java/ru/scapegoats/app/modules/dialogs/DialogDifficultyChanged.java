package ru.scapegoats.app.modules.dialogs;

import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.settings.SettingsActivity;

public class DialogDifficultyChanged {
    private SettingsActivity activity;

    public DialogDifficultyChanged(SettingsActivity activity) {
        this.activity = activity;
    }

    public AlertDialog getDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(R.string.settingsDialogText);
        builder.setTitle(R.string.settingsDialogTitle);
        builder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.startNewGameAfterDifficultyChanged= SettingsActivity.DO_NOT_START_NEW_GAME;
                activity.onBackPressed();
            }
        });

        builder.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.startNewGameAfterDifficultyChanged = SettingsActivity.START_NEW_GAME;
                activity.onBackPressed();
            }
        });
        return builder.create();
    }

}
