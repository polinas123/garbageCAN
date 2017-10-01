package com.sillyv.garbagecan.data;

import android.content.Context;
import android.util.SparseArray;

import com.sillyv.garbagecan.data.location.LatLonModel;
import com.sillyv.garbagecan.screen.camera.FileUploadEvent;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Vasili on 9/15/2017.
 *
 */

public interface RepositoryContract {
    interface Database {
        Completable saveRecord(FileUploadEvent fileUploadEvent);
    }

    interface Image {
        Single<String> uploadPhotoRx(FileUploadEvent fileUploadEvent);
    }

    interface Location {
        Single<LatLonModel> getLocation();

        void detach();
    }

    interface Credentials {
        Single<SparseArray<String>> getCredentialsRx();
    }

     interface Email {
         Completable sendEmailRx(FileUploadEvent fileUploadEvent);

         Completable sendEmailViaIntentServiceRx(Context context,
                                                 FileUploadEvent fileUploadEvent);
     }

    public interface Decryption {
        Single<FileUploadEvent> massDecryptRx(FileUploadEvent fileUploadEvent);
    }
}
