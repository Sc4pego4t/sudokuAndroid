package ru.scapegoats.app.activity.main;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.help.HelpActivity;
import ru.scapegoats.app.activity.main.game.GameInteractions;
import ru.scapegoats.app.activity.settings.SettingsActivity;
import ru.scapegoats.app.modules.Presenter;
import ru.scapegoats.app.modules.SettingsPreferences;
import ru.scapegoats.app.modules.dialogs.DialogStartNewGame;

public class MainPresenter implements Presenter<MainView> {

    MainActivity activity;
    MainView view;
    GameInteractions gameInteractions;
    @Override
    public void onViewAttached(@NonNull MainView view) {
        this.view=view;
        activity = view.activity;
        gameInteractions = new GameInteractions(activity);
        loadSettings();
        gameInteractions.startGame(view.buttonContainer1, view.container);

    }

    void optionItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.back: gameInteractions.backToPreviousState();break;
            case R.id.settings:
                activity.startActivityForResult(new Intent(activity,SettingsActivity.class),0);
                break;
            case R.id.help:
                activity.startActivity(new Intent(activity,HelpActivity.class));
                break;
            case R.id.erase:gameInteractions.eraseCell();break;
            case R.id.new_game: DialogStartNewGame.showDialog(activity);break;
        }
    }

    public void startNewGame(){
        view.clearView();
        onViewAttached(view);
    }

    void loadSettings(){
        gameInteractions.setSettings(SettingsPreferences.getSettings(activity));
    }

    @Override
    public void onViewDetached() {

    }

    @Override
    public void onDestroyed() {

    }

}
