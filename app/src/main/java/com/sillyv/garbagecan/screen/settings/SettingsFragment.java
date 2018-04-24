package com.sillyv.garbagecan.screen.settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sillyv.garbagecan.R;
import com.sillyv.garbagecan.screen.navigation.Navigator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment
        extends Fragment
        implements SettingsContract.View {


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SettingsContract.Presenter presenter = new SettingsPresenter(Navigator.getInstance((AppCompatActivity) getActivity()));
        initViews();
        presenter.init(this);
        return view;
    }

    private void initViews() {
//        View historyButton = view.findViewById(R.id.show_history_button);
//        historyButton.setOnClickListener(view1 -> presenter.showHistory());
    }

}
