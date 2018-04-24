package com.sillyv.garbagecan.screen.main;

import android.os.Bundle;

/**
 * Created by Vasili on 9/20/2017.
 *
 */

public interface MainContract {

    interface View  {
        void displayApology();

        void promptForSignUp(Bundle savedInstanceState);
    }

    interface Presenter {
        void init(Bundle savedInstanceState);

        void onBackPressed(MainActivity mainActivity);

        void handleSignUpResponse(Boolean aBoolean, Bundle savedInstanceState);
    }

}
