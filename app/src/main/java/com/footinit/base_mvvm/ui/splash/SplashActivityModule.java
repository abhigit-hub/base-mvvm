package com.footinit.base_mvvm.ui.splash;

import android.arch.lifecycle.ViewModelProvider;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 04-12-2017.
 */

@Module
public class SplashActivityModule {

    @Provides
    SplashViewModel providesSplashViewModel(DataManager dataManager,
                                            SchedulerProvider schedulerProvider,
                                            NetworkUtils networkUtils) {
        return new SplashViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory splashViewModelProvider(SplashViewModel splashViewModel) {
        return new ViewModelProviderFactory<>(splashViewModel);
    }
}
