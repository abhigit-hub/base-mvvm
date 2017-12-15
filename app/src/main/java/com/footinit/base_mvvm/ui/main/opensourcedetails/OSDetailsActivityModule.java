package com.footinit.base_mvvm.ui.main.opensourcedetails;

import android.arch.lifecycle.ViewModelProvider;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 10-12-2017.
 */

@Module
public class OSDetailsActivityModule {

    @Provides
    OSDetailsViewModel providesOSDetailsViewModel(DataManager dataManager,
                                                  SchedulerProvider schedulerProvider,
                                                  NetworkUtils networkUtils) {
        return new OSDetailsViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory osDetailsViewModelProvider(OSDetailsViewModel osDetailsViewModel) {
        return new ViewModelProviderFactory<>(osDetailsViewModel);
    }
}
