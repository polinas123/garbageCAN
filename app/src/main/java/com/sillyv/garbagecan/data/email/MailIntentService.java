package com.sillyv.garbagecan.data.email;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.sillyv.garbagecan.data.credentials.CredentialsManager;
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
    public static final String EXTRA_SENDER = "EXTRA_SENDER";
    public static final String EXTRA_SENDER_PASSWORD = "EXTRA_SENDER_PASSWORD";
    public static final String EXTRA_RECIPIENT = "EXTRA_RECIPIENT";
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
            emailRepo.sendEmail(getFileUploadEventFromIntent(intent));
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
        SparseArray<String> credentialsMap = new SparseArray<>();
        credentialsMap.put(CredentialsManager.SENDER, intent.getStringExtra(EXTRA_SENDER));
        credentialsMap.put(CredentialsManager.SENDER_PASSWORD,
                intent.getStringExtra(EXTRA_SENDER_PASSWORD));
        credentialsMap.put(CredentialsManager.RECIPIENT, intent.getStringExtra(EXTRA_RECIPIENT));



        fileUploadEvent.setCredentialsMap(credentialsMap);
        return fileUploadEvent;
    }
}
