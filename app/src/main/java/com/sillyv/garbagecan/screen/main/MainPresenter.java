package com.sillyv.garbagecan.screen.main;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import com.sillyv.garbagecan.core.BasePresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Vasili on 9/20/2017.
 *
 */

public class MainPresenter
        extends BasePresenter
        implements MainContract.Presenter {

    private MainContract.View view;

    MainPresenter(MainContract.View view) {
        this.view = view;
    }


    public void init(Activity activity, Bundle savedInstanceState) {
        registerDisposable(
                new RxPermissions(activity).request(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                        .subscribeWith(new DisposableObserver<Boolean>() {
                            @Override
                            public void onNext(Boolean granted) {
                                if (granted) {
                                    if (savedInstanceState == null) {
                                        view.displayCamera();
                                    }
                                } else {
                                    view.displayApology();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));


    }


}
