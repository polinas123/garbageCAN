package com.sillyv.garbagecan.di;

import com.sillyv.garbagecan.data.RepositoryContract;
import com.sillyv.garbagecan.data.database.DataBaseRepo;
import com.sillyv.garbagecan.data.email.EmailRepo;
import com.sillyv.garbagecan.data.images.ImageRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vasili on 10/5/2017.
 */

@Module
public class NetworkModule {


    public NetworkModule() {
    }

    @Provides
    @Singleton
    RepositoryContract.Email providesEmail(EmailRepo email) {
        return email;
    }


    @Provides
    @Singleton
    RepositoryContract.Image providesEmail(ImageRepo image) {
        return image;
    }


    @Provides
    @Singleton
    RepositoryContract.Database providesEmail(DataBaseRepo dataBase) {
        return dataBase;
    }

}
