package ru.scapegoats.app.activity.help;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

import androidx.annotation.Nullable;

import ru.scapegoats.app.R;
import ru.scapegoats.app.activity.help.fragments.HelpFragment3;
import ru.scapegoats.app.modules.AbstractActivity;
import ru.scapegoats.app.modules.Presenter;
import ru.scapegoats.app.modules.Swipable;

public class HelpActivity extends AbstractActivity<HelpView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.help_layout);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

    }

    @Override
    protected Presenter initPresenter() {
        return new HelpPresenter();
    }

    @Override
    protected HelpView initView() {
        return new HelpView(this,findViewById(android.R.id.content));
    }


    @Override
    protected Swipable initSwipe() {
        return new HelpSwipe(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
