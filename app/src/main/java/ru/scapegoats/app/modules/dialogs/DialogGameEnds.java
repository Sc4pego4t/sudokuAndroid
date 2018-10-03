package ru.scapegoats.app.modules.dialogs;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.activity.main.MainPresenter;

public class DialogGameEnds {

    public static void showDialog(final MainActivity activity){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(R.string.wonDialogText);
        builder.setTitle(R.string.gameTitle);

        builder.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainPresenter)activity.presenter).startNewGame();
            }
        });
        builder.create().show();
    }

}
