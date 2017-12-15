package com.footinit.base_mvvm.ui.login;

import android.arch.lifecycle.ViewModelProvider;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 07-12-2017.
 */


@Module
public class LoginActivityModule {

    @Provides
    LoginViewModel providesLoginViewModel(DataManager dataManager,
                                          SchedulerProvider schedulerProvider,
                                          NetworkUtils networkUtils) {
        return new LoginViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory loginViewModelProvider(LoginViewModel loginViewModel) {
        return new ViewModelProviderFactory<>(loginViewModel);
    }
}
