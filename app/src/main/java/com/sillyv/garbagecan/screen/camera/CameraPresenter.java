package com.sillyv.garbagecan.screen.camera;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sillyv.garbagecan.core.BasePresenter;
import com.sillyv.garbagecan.data.location.LatLonModel;
import com.sillyv.garbagecan.screen.navigation.Navigator;
import com.sillyv.garbagecan.util.HappinessColorMapper;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Class created by Vasili on 9/15/2017.
 */

class CameraPresenter
        extends BasePresenter
        implements CameraContract.Presenter {


    private static final String TAG = "CameraPresenter";
    private final Navigator navigator;
    private CameraContract.View view;
    private CameraContract.Repo repo;
    private File lastPhotoFileName;

    CameraPresenter(CameraContract.View view,
                    CameraContract.Repo repo, Navigator navigator) {
        this.view = view;
        this.repo = repo;
        this.navigator = navigator;
    }


    @Override
    public void subscribeToEvents() {
//        view.getSavedFile()

    }

    @Override
    public void notifyPhotoSaved(Context context, FileUploadEvent uploadEvent) {
        Observable.just(uploadEvent).doOnSubscribe(this::registerDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(fileUploadEvent -> view.notifyImageBeingSent(HappinessColorMapper
                                .getHappinessFromButton(fileUploadEvent.getScore()),
                        fileUploadEvent.getFile()))
                .doOnNext(fileUploadEvent -> view.hideButtons())
                .observeOn(Schedulers.io())
                .flatMapSingle(fileUploadEvent -> repo.getLocation()
                        .map(CameraPresenter.this.injectLocationIntoUploadModel(fileUploadEvent)))
                .doOnNext(fileUploadEvent -> Log.d(TAG,
                        System.currentTimeMillis() + "After Location"))
                .observeOn(Schedulers.io())
                .flatMapSingle(fileUploadEvent -> repo.sendEmail(context, fileUploadEvent)
                        .toSingle(() -> fileUploadEvent))
                .doOnNext(fileUploadEvent -> Log.d(TAG,
                        System.currentTimeMillis() + "After Sending"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FileUploadEvent>() {
                    @Override
                    public void onNext(FileUploadEvent file) {
                        lastPhotoFileName = file.getFile();
                        view.showLastPhotoTaken(lastPhotoFileName);
                        view.hideProgressBar();
                        view.displayThankYouDialog();
                        Log.d(TAG,
                                "onNext: FilePath: " + file.getFile().getPath() +
                                        ", Score " + file.getScore() +
                                        ", Lat: " + file.getLatitude() +
                                        ", Lon: " + file.getLongitude()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void navigateToSettings() {
        navigator.openSettings();
    }

    @Override
    public void navigateToAdditionalInfo() {
        if (lastPhotoFileName != null) {
            navigator.openAdditionalInfo(lastPhotoFileName);
        }
    }

    @NonNull
    private Function<LatLonModel, FileUploadEvent> injectLocationIntoUploadModel(FileUploadEvent cameraEventModel) {
        return latLonModel -> {
            cameraEventModel.setLatitude(latLonModel.getLatitude());
            cameraEventModel.setLongitude(latLonModel.getLongitude());
            return cameraEventModel;
        };
    }

    @Override
    public void detach() {
        super.detach();
        repo.detach();

    }
}
