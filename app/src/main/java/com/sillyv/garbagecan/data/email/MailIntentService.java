package com.sillyv.garbagecan.data.email;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sillyv.garbagecan.data.credentials.CredentialsManager;
import com.sillyv.garbagecan.data.encryption.EncryptionRepo;
import com.sillyv.garbagecan.screen.camera.FileUploadEvent;

import java.io.File;

import javax.mail.MessagingException;

/**
 * Created by Vasili on 10/1/2017.
 *
 */

public class MailIntentService
        extends IntentService {

    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    public static final String EXTRA_SCORE = "EXTRA_SCORE";
    public static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";
    public static final String MAIL_INTENT_SERVICE_LABEL = "MailIntentService";
    public static boolean isIntentServiceRunning = false;

    public MailIntentService() {
        super(MAIL_INTENT_SERVICE_LABEL);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isIntentServiceRunning = true;
        EmailRepo emailRepo = new EmailRepo();
        try {
            String to = CredentialsManager.ENCRYPTED_RECIPIENT;
            String from = CredentialsManager.ENCRYPTED_SENDER;
            String password = CredentialsManager.ENCRYPTED_PASSWORD;
            to = EncryptionRepo.decrypt(to, EncryptionRepo.RECIPIENT);
            from = EncryptionRepo.decrypt(from, EncryptionRepo.SENDER);
            password = EncryptionRepo.decrypt(password, EncryptionRepo.PASSWORD);
            emailRepo.sendEmail(getFileUploadEventFromIntent(intent), to, from, password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        isIntentServiceRunning = false;
    }

    @NonNull
    private FileUploadEvent getFileUploadEventFromIntent(Intent intent) {
        FileUploadEvent fileUploadEvent = new FileUploadEvent(new File(intent.getStringExtra(
                EXTRA_FILE_PATH)), intent.getIntExtra(EXTRA_SCORE, -1));
        fileUploadEvent.setLatitude(intent.getDoubleExtra(EXTRA_LATITUDE, 0));
        fileUploadEvent.setLongitude(intent.getDoubleExtra(EXTRA_LONGITUDE, 0));
        return fileUploadEvent;
    }
}
