package com.footinit.base_mvvm;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.evernote.android.job.JobManager;
import com.facebook.FacebookSdk;
import com.footinit.base_mvvm.di.component.AppComponent;
import com.footinit.base_mvvm.di.component.DaggerAppComponent;
import com.footinit.base_mvvm.services.jobs.MVVMJobCreator;
import com.footinit.base_mvvm.utils.AppLogger;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MvvmApp extends Application implements HasActivityInjector, HasServiceInjector {

    private AppComponent appComponent;


    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //Instantiate AppComponent
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);

        AppLogger.init();


        /*
        * Init Facebook SDK*/
        FacebookSdk.sdkInitialize(getApplicationContext());

        CalligraphyConfig.initDefault(mCalligraphyConfig);

        JobManager.create(this).addJobCreator(new MVVMJobCreator());
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }
}
