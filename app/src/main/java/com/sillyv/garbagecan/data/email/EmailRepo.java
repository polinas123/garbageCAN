package com.sillyv.garbagecan.data.email;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.sillyv.garbagecan.data.RepositoryContract;
import com.sillyv.garbagecan.data.credentials.CredentialsManager;
import com.sillyv.garbagecan.screen.camera.FileUploadEvent;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.reactivex.Completable;


/**
 * Created by Vasili on 9/20/2017.
 */
@ParametersAreNonnullByDefault
public class EmailRepo
        implements RepositoryContract.Email {


    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIN_SMTP_PORT_VALUE = "587";
    private static final String MAIN_SMTP_HOST_VALUE = "smtp.gmail.com";
    private static final String MAIL_SMTP_STARTTLS_ENABLE_VALUE = "true";
    private static final String MAIL_SMTP_AUTH_VALUE = "true";
    private static final String MESSAGE_SUBJECT = "Let me know how this should be formatted";

    @Inject
    public EmailRepo() {
    }

    public void sendEmail(FileUploadEvent fileUploadEvent) throws MessagingException {

        SparseArray<String> credentialsMap = fileUploadEvent.getCredentialsMap();
        final String username = credentialsMap.get(CredentialsManager.SENDER);
        final String password = credentialsMap.get(CredentialsManager.SENDER_PASSWORD);

        Properties props = new Properties();
        props.put(MAIL_SMTP_AUTH, MAIL_SMTP_AUTH_VALUE);
        props.put(MAIL_SMTP_STARTTLS_ENABLE, MAIL_SMTP_STARTTLS_ENABLE_VALUE);
        props.put(MAIL_SMTP_HOST, MAIN_SMTP_HOST_VALUE);
        props.put(MAIL_SMTP_PORT, MAIN_SMTP_PORT_VALUE);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(username));

        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(credentialsMap.get(CredentialsManager.RECIPIENT)));


        // Set Subject: header field
        message.setSubject(MESSAGE_SUBJECT);

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        String message_body = "Latitude: " + fileUploadEvent.getLatitude() + "\n" +
                "Longitude: " + fileUploadEvent.getLongitude() + "\n" +
                "Time: " + new Date().toString() + "\n" +
                "Score: " + fileUploadEvent.getScore();

        messageBodyPart.setText(message_body);

        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String filename = fileUploadEvent.getFile().getPath();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        message.setContent(multipart);

//         Send message a
        Transport.send(message);


    }

    @Override
    public Completable sendEmailRx(FileUploadEvent fileUploadEvent) {
        return Completable.fromAction(() -> sendEmail(fileUploadEvent));
    }


    @Override
    public Completable sendEmailViaIntentServiceRx(Context context,
                                                   FileUploadEvent fileUploadEvent) {
        return Completable.fromAction(() -> startIntentService(context, fileUploadEvent));
    }

    private void startIntentService(Context context, FileUploadEvent fileUploadEvent) {
        Intent intent = new Intent(context, MailIntentService.class);
        intent.putExtra(MailIntentService.EXTRA_LATITUDE, fileUploadEvent.getLatitude());
        intent.putExtra(MailIntentService.EXTRA_LONGITUDE, fileUploadEvent.getLongitude());
        intent.putExtra(MailIntentService.EXTRA_SCORE, fileUploadEvent.getScore());
        intent.putExtra(MailIntentService.EXTRA_FILE_PATH, fileUploadEvent.getFile().getPath());
        SparseArray<String> credentialsMap = fileUploadEvent.getCredentialsMap();
        intent.putExtra(MailIntentService.EXTRA_SENDER,
                credentialsMap.get(CredentialsManager.SENDER));
        intent.putExtra(MailIntentService.EXTRA_SENDER_PASSWORD,
                credentialsMap.get(CredentialsManager.SENDER_PASSWORD));
        intent.putExtra(MailIntentService.EXTRA_RECIPIENT,
                credentialsMap.get(CredentialsManager.RECIPIENT));
        context.startService(intent);
    }
}
