package com.sillyv.garbagecan.screen.main;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vasili on 9/20/2017.
 *
 */

public interface MainContract {


    interface View  {
        void displayCamera();

        void displayApology();

        void finish();
    }


    interface Presenter {
        void init(Activity mainActivity, Bundle savedInstanceState);
    }

    interface Navigator {
        void openCamera(Activity windowManager);

        void onBackPressed(MainContract.View activity);
    }
}
