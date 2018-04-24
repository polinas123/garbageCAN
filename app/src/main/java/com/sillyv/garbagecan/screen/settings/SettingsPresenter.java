package com.sillyv.garbagecan.screen.settings;

import com.sillyv.garbagecan.screen.navigation.Navigator;

/**
 * Class created by Vasili on 10/22/2017.
 */

public class SettingsPresenter
        implements SettingsContract.Presenter {


    private final Navigator instance;

    SettingsPresenter(Navigator instance) {
        this.instance = instance;
    }

    @Override
    public void init(SettingsContract.View view) {

    }

    @Override
    public void showHistory() {
        instance.openHistory();
    }
}
