package com.sillyv.garbagecan.screen.settings;

/**
 * Created by Vasili on 10/22/2017.
 */

public class SettingsContract {

    interface View {
    }

    interface Presenter {
        void init(SettingsContract.View settingsFragment);

        void showHistory();
    }

    public interface Navigator {
        void openHistory();
    }
}
