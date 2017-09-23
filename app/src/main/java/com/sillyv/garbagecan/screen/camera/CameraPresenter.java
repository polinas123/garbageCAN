package com.sillyv.garbagecan.screen.camera;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.sillyv.garbagecan.core.BasePresenter;
import com.sillyv.garbagecan.data.location.LatLonModel;
import com.sillyv.garbagecan.util.HappinessColorMapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Vasili on 9/15/2017.
 */

class CameraPresenter
        extends BasePresenter
        implements CameraContract.Presenter {


    private static final String TAG = "CameraPresenter";
    private CameraContract.View view;
    private CameraContract.Repo repo;

    public CameraPresenter(CameraContract.View view,
                            CameraContract.Repo repo) {
        this.view = view;
        this.repo = repo;
    }




    @Override
    public void subscribeToEvents() {
//        view.getSavedFile()

    }

    @Override
    public void notifyPhotoSaved(FileUploadEvent uploadEvent) {
        Observable.just(uploadEvent).doOnSubscribe(this::registerDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(fileUploadEvent -> view.notifyImageBeingSent(HappinessColorMapper
                                .getHappinessFromButton(fileUploadEvent.getScore()),
                        fileUploadEvent.getFile()))
                .doOnNext(fileUploadEvent -> view.hideButtons())
                .observeOn(Schedulers.io())
                .flatMapSingle(fileUploadEvent -> repo.getLocation()
                        .map(CameraPresenter.this.injectLocationIntoUploadModel(fileUploadEvent)))
                .flatMapSingle(fileUploadEvent -> repo.getCredentials()
                        .map(CameraPresenter.this.injectCredentialsIntoUploadModel(fileUploadEvent)))
                .flatMapSingle(fileUploadEvent -> repo.decryptCredentials(fileUploadEvent))
                .observeOn(Schedulers.io())
                .flatMapSingle(fileUploadEvent -> repo.sendEmail(fileUploadEvent)
                        .toSingle(() -> fileUploadEvent))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FileUploadEvent>() {
                    @Override
                    public void onNext(FileUploadEvent file) {
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

    private Function<SparseArray<String>, FileUploadEvent> injectCredentialsIntoUploadModel(
            FileUploadEvent fileUploadEvent) {
        return integerStringMap -> {
            fileUploadEvent.setCredentialsMap(integerStringMap);
            return fileUploadEvent;
        };
    }

    @NonNull
    private Function<String, FileUploadEvent> injectRemotePathIntoUploadModel(FileUploadEvent cameraEventModel) {
        return remotePath -> {
            cameraEventModel.setUploadedFilePath(remotePath);
            return cameraEventModel;
        };
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
