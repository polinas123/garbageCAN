package com.sillyv.garbagecan.screen.navigation;

import android.app.Activity;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.sillyv.garbagecan.screen.camera.CameraContract;
import com.sillyv.garbagecan.screen.camera.CameraFragment;
import com.sillyv.garbagecan.screen.main.MainContract;
import com.sillyv.garbagecan.screen.settings.SettingsFragment;

/**
 * Created by Vasili on 10/10/2017.
 */

public class Navigator
        implements CameraContract.Navigation, MainContract.Navigator {

    private static Navigator instance;
    private FragmentManager manager;
    private Integer container;

    private Navigator() {
    }

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    public void attach(FragmentManager manager, Integer container) {
        this.manager = manager;
        this.container = container;
    }

    public void detach() {
        this.manager = null;
    }

    @Override
    public void openSettings() {
        Fragment fragment = new SettingsFragment();
        if (manager == null || container == null) {
            throw new RuntimeException("Please Instanciate Fragment manager");
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void openCamera(Activity activity) {
        CameraFragment fragment = getCameraFragment(activity.getWindowManager());
        manager.beginTransaction()
                .add(container, fragment).commit();
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
}
