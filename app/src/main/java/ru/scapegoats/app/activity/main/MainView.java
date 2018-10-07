package ru.scapegoats.app.activity.main;

import android.view.View;
import android.widget.LinearLayout;

import ru.scapegoats.app.R;
import ru.scapegoats.app.modules.Viewable;

public class MainView implements Viewable {

    LinearLayout container,buttonContainer1;
    MainActivity activity;
    public LinearLayout general;
    MainView(MainActivity activity,View rootView){
        this.activity=activity;
        container=rootView.findViewById(R.id.container);
        buttonContainer1=rootView.findViewById(R.id.buttonContainer1);
        general=rootView.findViewById(R.id.general);
    }

    void clearView(){
        container.removeAllViews();
        buttonContainer1.removeAllViews();
    }

    @Override
    public void onAttach() {

    }
}
