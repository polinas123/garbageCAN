package com.sillyv.garbagecan.di;

import com.sillyv.garbagecan.data.RepositoryContract;
import com.sillyv.garbagecan.data.credentials.CredentialsManager;
import com.sillyv.garbagecan.data.encryption.EncryptionRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vasili on 10/5/2017.
 */
@Module
public class LocalModule {

    public LocalModule() {
    }

    @Provides
    @Singleton
    RepositoryContract.Credentials providesEmail(CredentialsManager credentialsManager) {
        return credentialsManager;
    }


    @Provides
    @Singleton
    RepositoryContract.Decryption providesEmail(EncryptionRepo encryptionRepo) {
        return encryptionRepo;
    }
}
