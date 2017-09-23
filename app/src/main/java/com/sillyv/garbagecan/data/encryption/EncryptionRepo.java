package com.sillyv.garbagecan.data.encryption;

import android.support.annotation.NonNull;

import com.sillyv.garbagecan.data.RepositoryContract;
import com.sillyv.garbagecan.data.credentials.CredentialsManager;
import com.sillyv.garbagecan.screen.camera.FileUploadEvent;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import io.reactivex.Single;

/**
 * Created by Vasili on 9/23/2017.
 */

public class EncryptionRepo
        implements RepositoryContract.Decryption {

    private static String PASSWORD = "MakeAmericaGreatAgain1234";
    private static String RECIPIENT = "JerusalemMunicipality@jerusalem.muni.il";
    private static String SENDER = "NirBarkat@jerusalem.muni.il";






    private static String decrypt(String encrypt, String seed) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(seed);
        return encryptor.decrypt(encrypt);
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
