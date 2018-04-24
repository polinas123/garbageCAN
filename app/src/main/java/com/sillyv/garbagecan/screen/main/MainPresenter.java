package com.sillyv.garbagecan.screen.main;

import android.Manifest;
import android.os.Bundle;

import com.sillyv.garbagecan.core.BasePresenter;
import com.sillyv.garbagecan.data.preferences.Prefs;
import com.sillyv.garbagecan.screen.navigation.Navigator;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Class created by Vasili on 9/20/2017.
 */

public class MainPresenter
        extends BasePresenter
        implements MainContract.Presenter {

    private MainContract.View view;
    private Navigator navigator;
    private RxPermissions rxPermissions;
    private Prefs prefs;

    MainPresenter(MainContract.View view, Navigator navigator, RxPermissions rxPermissions, Prefs prefs) {
        this.view = view;
        this.navigator = navigator;
        this.rxPermissions = rxPermissions;
        this.prefs = prefs;
    }


    public void init(Bundle savedInstanceState) {
        getPermissionObservable()
                .subscribe(permissionGranted -> handlePermissionResponse(savedInstanceState, permissionGranted));
    }

    private void handlePermissionResponse(Bundle savedInstanceState, Boolean granted) {
        if (granted) {
            handlePositiveResponse(savedInstanceState);
        } else {
            handleNegativeResponse();
        }
    }

    private void handleNegativeResponse() {
        displayApology();
    }

    private void handlePositiveResponse(Bundle savedInstanceState) {
        Single.fromCallable(() -> prefs.readFirstTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(firstTime -> handlePrefsFirstTimeResult(firstTime, savedInstanceState));
    }

    private void handlePrefsFirstTimeResult(Boolean firstTime, Bundle savedInstanceState) {
        if (firstTime) {
            promptForSignUp(savedInstanceState);
        } else {
            attemptCameraOpen(savedInstanceState);
        }
    }

    private void promptForSignUp(Bundle savedInstanceState) {
        view.promptForSignUp(savedInstanceState);
    }

    private Observable<Boolean> getPermissionObservable() {
        return rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .doOnSubscribe(this::registerDisposable);
    }

    private void displayApology() {
        view.displayApology();
    }

    private void attemptCameraOpen(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            navigator.openCamera();
        }
    }

    @Override
    public void onBackPressed(MainActivity mainActivity) {
        navigator.onBackPressed();
    }

    @Override
    public void handleSignUpResponse(Boolean response, Bundle savedInstanceState) {

        if (response) {
            goToSettings();
        } else {
            attemptCameraOpen(savedInstanceState);
        }
    }

    private void goToSettings() {
        navigator.openSettings();
    }
}
