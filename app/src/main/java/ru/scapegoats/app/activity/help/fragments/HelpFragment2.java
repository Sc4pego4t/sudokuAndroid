package ru.scapegoats.app.activity.help.fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.scapegoats.app.R;

public class HelpFragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.view_help_2, null);

        Resources resources = getContext().getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.cellclick))
                .appendPath(resources.getResourceTypeName(R.drawable.cellclick))
                .appendPath(resources.getResourceEntryName(R.drawable.cellclick))
                .build();


        Log.e("URI",uri.toString());

        SimpleDraweeView draweeView = view.findViewById(R.id.my_image_view);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        draweeView.setController(controller);

        return view;
    }
}
