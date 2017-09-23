package com.sillyv.garbagecan.data.encryption;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

/**
 * Created by Vasili on 9/23/2017.
 */
public class EncryptionRepoTest {


    private String seed = "NirBarkat@jerusalem.muni.il";


    private static final String SENDER_VALUE = "aaa";
    private static final String SENDER_PASSWORD_VALUE = "aaa";
    private static final String RECIPIENT_VALUE = "aaa";
    private static final String PASSWORD = "aaa";
    private static final String RECIPIENT = "aaa";
    private static final String SENDER = "aaa";


    @Test
    public void encryptDecript() throws Exception {
        String str = "aaa";

        System.out.println(SENDER_VALUE + ": " + encrypt(SENDER_VALUE, SENDER));
        System.out.println(SENDER_PASSWORD_VALUE + ": " + encrypt(SENDER_PASSWORD_VALUE, PASSWORD));
        System.out.println(RECIPIENT_VALUE + ": " + encrypt(RECIPIENT_VALUE, RECIPIENT));


    }

    private String decrypt(String encrypt) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(seed);
        return encryptor.decrypt(encrypt);
    }

    private String encrypt(String str, String seed) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(seed);
        return encryptor.encrypt(str);

    }



}