package com.sillyv.garbagecan.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vasili on 10/5/2017.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }


    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}



