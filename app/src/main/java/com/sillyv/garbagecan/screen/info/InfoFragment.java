package com.sillyv.garbagecan.screen.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sillyv.garbagecan.R;
import com.sillyv.garbagecan.screen.main.MainContract;
import com.sillyv.garbagecan.screen.navigation.Navigator;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Class created by Vasili on 10/22/2017.
 */

public class InfoFragment
        extends Fragment {


    public static final String PATH_EXTRA = "PATH";
    private final Navigator navigator = Navigator.getInstance((AppCompatActivity) getActivity());

    public static InfoFragment getInstance(String path) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH_EXTRA, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ImageView imageView = view.findViewById(R.id.image_preview);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String path = arguments.getString(PATH_EXTRA);
            if (path != null) {
                Picasso.with(getContext())
                        .load(new File(path))
                        .into(imageView);
            }
        }
        view.findViewById(R.id.update_button).setOnClickListener(view1 -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof MainContract.View) {
                navigator.onBackPressed();
            }
        });
        return view;
    }
}
