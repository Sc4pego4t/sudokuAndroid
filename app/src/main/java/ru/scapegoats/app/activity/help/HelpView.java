package ru.scapegoats.app.activity.help;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.main.MainActivity;
import ru.scapegoats.app.modules.Viewable;

public class HelpView implements Viewable {

    public HelpActivity activity;
    public FrameLayout viewContainer;
    public RadioGroup radioGroup;
    public RadioButton rb1, rb2, rb3;

    HelpView(HelpActivity activity,View rootView){
        this.activity=activity;
        viewContainer=rootView.findViewById(R.id.viewContainer);
        radioGroup=rootView.findViewById(R.id.radioGroup);
        rb1=rootView.findViewById(R.id.rb1);
        rb2=rootView.findViewById(R.id.rb2);
        rb3=rootView.findViewById(R.id.rb3);
    }
    @Override
    public void onAttach() {

    }
}
