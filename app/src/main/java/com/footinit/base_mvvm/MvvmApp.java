/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.footinit.base_mvvm;

import android.app.Activity;
import android.app.Application;

import com.facebook.FacebookSdk;
import com.footinit.base_mvvm.di.component.AppComponent;
import com.footinit.base_mvvm.di.component.DaggerAppComponent;
import com.footinit.base_mvvm.utils.AppLogger;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MvvmApp extends Application implements HasActivityInjector {

    private AppComponent appComponent;


    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

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
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
