package ru.scapegoats.app.activity.help;


import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;


import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;
import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.help.fragments.HelpFragment1;
import ru.scapegoats.app.activity.help.fragments.HelpFragment2;
import ru.scapegoats.app.activity.help.fragments.HelpFragment3;
import ru.scapegoats.app.modules.Presenter;

public class HelpPresenter implements Presenter<HelpView> {

    HelpActivity activity;
    HelpView view;
    @Override
    public void onViewAttached(HelpView view) {
        activity=view.activity;
        view.radioGroup.setOnCheckedChangeListener(new checkedChanged());
        view.rb1.setChecked(true);
        this.view=view;
        ActionBar bar=view.activity.getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(R.string.help);
    }

    FragmentTransaction fTrans;
    int lastSelectedRadioButton=0;

    class checkedChanged implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int idButton) {
            fTrans = activity.getSupportFragmentManager().beginTransaction();

            switch (idButton){
                case R.id.rb1:
                    if(lastSelectedRadioButton!=0)
                        fTrans.setCustomAnimations(R.anim.slide_from_up, R.anim.slide_to_down);
                    fTrans.replace(R.id.viewContainer, new HelpFragment1());
                    lastSelectedRadioButton=R.id.rb1;
                    break;
                case R.id.rb2:
                    if(lastSelectedRadioButton==R.id.rb1){
                        fTrans.setCustomAnimations(R.anim.slide_from_down, R.anim.slide_to_up);
                    } else {
                        fTrans.setCustomAnimations(R.anim.slide_from_up, R.anim.slide_to_down);
                    }
                    fTrans.replace(R.id.viewContainer, new HelpFragment2());
                    break;
                case R.id.rb3:
                    fTrans.setCustomAnimations(R.anim.slide_from_down, R.anim.slide_to_up);
                    fTrans.replace(R.id.viewContainer, new HelpFragment3());
                    lastSelectedRadioButton=R.id.rb3;
                    break;
            }
            fTrans.commit();
        }
    }
    void selectLess(){
        if(view.rb1.isChecked()){
            view.rb2.setChecked(true);
        } else
        if(view.rb2.isChecked()){
            view.rb3.setChecked(true);
        }
    }
    void selectBigger(){
        if(view.rb3.isChecked()){
            view.rb2.setChecked(true);
        } else
        if(view.rb2.isChecked()){
            view.rb1.setChecked(true);
        }
    }


    @Override
    public void onViewDetached() {

    }

    @Override
    public void onDestroyed() {

    }
}
