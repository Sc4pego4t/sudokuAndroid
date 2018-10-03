package ru.scapegoats.app.activity.help.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import ru.scapegoats.app.R;

public class HelpFragment3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("created",";");
        return inflater.inflate(R.layout.view_help_3, null);
    }
}
