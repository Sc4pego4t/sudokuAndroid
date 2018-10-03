package ru.scapegoats.app.activity.help;

import ru.scapegoats.app.modules.Swipable;

public class HelpSwipe implements Swipable {
    HelpActivity activity;

    HelpSwipe(HelpActivity activity){
        this.activity=activity;
    }

    @Override
    public void onLeftSwipe() {

    }

    @Override
    public void onRightSwipe() {
        activity.onBackPressed();
    }

    @Override
    public void onUpSwipe() {
        ((HelpPresenter)activity.presenter).selectLess();
    }

    @Override
    public void onDownSwipe() {
        ((HelpPresenter)activity.presenter).selectBigger();
    }
}
