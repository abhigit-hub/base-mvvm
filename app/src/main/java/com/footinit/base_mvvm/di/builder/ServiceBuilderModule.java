package com.footinit.base_mvvm.di.builder;

import com.footinit.base_mvvm.services.service.SyncService;
import com.footinit.base_mvvm.services.service.SyncServiceTest;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract SyncService bindSyncService();

    @ContributesAndroidInjector
    abstract SyncServiceTest bindSyncServiceTest();
}