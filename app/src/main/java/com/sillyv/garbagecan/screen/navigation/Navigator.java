package com.sillyv.garbagecan.screen.navigation;

import android.app.Activity;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.sillyv.garbagecan.screen.camera.CameraContract;
import com.sillyv.garbagecan.screen.camera.CameraFragment;
import com.sillyv.garbagecan.screen.history.HistoryFragment;
import com.sillyv.garbagecan.screen.info.InfoFragment;
import com.sillyv.garbagecan.screen.settings.SettingsContract;
import com.sillyv.garbagecan.screen.settings.SettingsFragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class created by Vasili on 10/10/2017.
 */

public class Navigator
        implements CameraContract.Navigation, SettingsContract.Navigator {

    private static final String CAMERA_BACKSTACK_TAG = "Camera";
    private static final String SETTINGS_BACKSTACK_TAG = "SETTINGS_BACKSTACK_TAG";
    private static final String HISTORY_BACKSTACK_TAG = "History_backstack_tag";
    private static final String ADDITIONAL_INFO_BACKSTACK_TAG = "ADDITIONAL_INFO_BACKSTACK_TAG";

    private static Map<String, Navigator> navigationMap = new HashMap<>();
    private FragmentManager manager;
    private Integer container;
    private Activity activity;

    private Navigator(AppCompatActivity activity) {
        this.activity = activity;
        manager = activity.getSupportFragmentManager();
    }

    public static Navigator getInstance(AppCompatActivity activity) {
        Navigator navigator = navigationMap.get(activity.getClass().getCanonicalName());
        if (navigator == null) {
            navigator = new Navigator(activity);
            navigationMap.put(activity.getClass().getCanonicalName(), navigator);
        }
        return navigator;
    }

    @SuppressWarnings("unused")
    public void attach(FragmentManager manager, Integer container) {
        this.manager = manager;
        this.container = container;
    }

    @SuppressWarnings("unused")
    public void detach() {
        this.manager = null;
    }
    // TODO: 10/22/2017 Call this function from Main Activity

    @Override
    public void openSettings() {
        Fragment fragment = SettingsFragment.newInstance();
        if (manager == null || container == null) {
            throw new RuntimeException("Please Instantiate Fragment manager");
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.addToBackStack(SETTINGS_BACKSTACK_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void openAdditionalInfo(File lastPhotoFileName) {
        InfoFragment fragment = InfoFragment.getInstance(lastPhotoFileName.getPath());
        manager.beginTransaction().replace(container, fragment).addToBackStack(
                ADDITIONAL_INFO_BACKSTACK_TAG).commit();
    }


    public void openCamera() {
        CameraFragment fragment = getCameraFragment(activity.getWindowManager());
        manager.beginTransaction()
                .add(container, fragment).addToBackStack(CAMERA_BACKSTACK_TAG).commit();


    }


    public void onBackPressed() {
        if (manager.getBackStackEntryCount() == 1) {
            activity.finish();
        } else {
            manager.popBackStackImmediate();
        }
    }

    private CameraFragment getCameraFragment(WindowManager windowManager) {
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int width = Math.max(size.x, size.y);
        int height = Math.min(size.x, size.y);
        double maxRatio = (double) width / height;
        double minRatio = (double) (width) / height;
        return CameraFragment.newInstance(
                minRatio * .1, maxRatio, width, height);
    }

    @Override
    public void openHistory() {
        HistoryFragment historyFragment = new HistoryFragment();
        manager.beginTransaction().replace(container, historyFragment).addToBackStack(
                HISTORY_BACKSTACK_TAG).commit();
    }
}
