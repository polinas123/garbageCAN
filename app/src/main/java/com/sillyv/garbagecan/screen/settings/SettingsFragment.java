package com.sillyv.garbagecan.screen.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sillyv.garbagecan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment
        extends Fragment
        implements SettingsContract.View {


    private View historyArrowButton;
    private SettingsContract.Presenter presenter;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        presenter = new SettingsPresenter();
        initViews(view);
        presenter.init(this);
        return view;
    }

    private void initViews(View view) {
        View historyButton = view.findViewById(R.id.show_history_button);
        historyButton.setOnClickListener(view1 -> presenter.showHistory());
    }

}
