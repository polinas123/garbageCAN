package com.sillyv.garbagecan.data.encryption;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sillyv.garbagecan.data.RepositoryContract;
import com.sillyv.garbagecan.data.credentials.CredentialsManager;
import com.sillyv.garbagecan.screen.camera.FileUploadEvent;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import io.reactivex.Single;

/**
 * Created by Vasili on 9/23/2017.
 *
 */

public class EncryptionRepo
        implements RepositoryContract.Decryption {

    private static final String TAG = "Presenter";
    public static String PASSWORD = "MakeAmericaGreatAgain1234";
    public static String RECIPIENT = "JerusalemMunicipality@jerusalem.muni.il";
    public static String SENDER = "NirBarkat@jerusalem.muni.il";

    public static String decrypt(String encrypt, String seed) {
        Log.d(TAG, System.currentTimeMillis() + "Start Decrypt");
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(seed);
        String decrypt = encryptor.decrypt(encrypt);
        Log.d(TAG, System.currentTimeMillis() + "End Decrypt");
        return decrypt;
    }



    public Single<FileUploadEvent> massDecryptRx(FileUploadEvent fileUploadEvent) {
        return Single.just(fileUploadEvent)
                .map(fileUploadEvent1 -> decryptWithSeed(fileUploadEvent1,
                        CredentialsManager.SENDER,
                        SENDER))
                .map(fileUploadEvent1 -> decryptWithSeed(fileUploadEvent1,
                        CredentialsManager.RECIPIENT,
                        RECIPIENT))
                .map(fileUploadEvent1 -> decryptWithSeed(fileUploadEvent1,
                        CredentialsManager.SENDER_PASSWORD,
                        PASSWORD));
    }

    @NonNull
    private FileUploadEvent decryptWithSeed(FileUploadEvent fileUploadEvent1,
                                            int fieldType, String key) {
        String sender = fileUploadEvent1.getCredentialsMap().get(fieldType);
        sender = decrypt(sender, key);
        fileUploadEvent1.getCredentialsMap().put(fieldType, sender);
        return fileUploadEvent1;
    }


}
