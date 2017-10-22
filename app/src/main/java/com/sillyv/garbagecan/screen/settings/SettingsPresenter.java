package com.sillyv.garbagecan.screen.settings;

import com.sillyv.garbagecan.screen.navigation.Navigator;

/**
 * Created by Vasili on 10/22/2017.
 */

public class SettingsPresenter
        implements SettingsContract.Presenter {


    @Override
    public void init(SettingsContract.View view) {

    }

    @Override
    public void showHistory() {
        Navigator.getInstance().openHistory();
    }
}
