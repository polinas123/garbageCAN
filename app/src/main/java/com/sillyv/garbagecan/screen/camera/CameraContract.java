package com.sillyv.garbagecan.screen.camera;

import android.content.Context;
import android.util.SparseArray;

import com.sillyv.garbagecan.core.BaseContract;
import com.sillyv.garbagecan.data.location.LatLonModel;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Vasili on 9/15/2017.
 *
 */

public interface CameraContract {
    interface View {

        void takePicture();

        @SuppressWarnings("unused")
        boolean isFlashOn();

        Observable<FileUploadEvent> getSavedFile();

        void displayThankYouDialog();

        void notifyImageBeingSent(int happinessFromButton, File file);

        void  hideProgressBar();

        void hideButtons();

        void showLastPhotoTaken(File file);
    }


    interface Presenter extends BaseContract.Presenter {


        void subscribeToEvents();

        void notifyPhotoSaved(Context context, FileUploadEvent fileUploadEvent);

        void navigateToSettings();

        void navigateToAdditionalInfo();
    }

    interface Repo {
        Single<LatLonModel> getLocation();

        Single<String> uploadPhoto(FileUploadEvent fileUploadEvent);

        Completable saveNewRecord(FileUploadEvent fileUploadEvent);

        Single<SparseArray<String>> getCredentials();

        Completable sendEmail(Context context, FileUploadEvent fileUploadEvent);

        Single<FileUploadEvent> decryptCredentials(FileUploadEvent fileUploadEvent);

        void detach();
    }

    interface Navigation {
        void openSettings();

        void openAdditionalInfo(File lastPhotofileName);
    }
}
