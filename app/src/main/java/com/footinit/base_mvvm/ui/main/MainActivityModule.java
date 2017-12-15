package com.footinit.base_mvvm.ui.main;

import android.arch.lifecycle.ViewModelProvider;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 08-12-2017.
 */

@Module
public class MainActivityModule {

    @Provides
    MainViewModel providesMainViewModel(DataManager dataManager,
                                        SchedulerProvider schedulerProvider,
                                        NetworkUtils networkUtils) {
        return new MainViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    MainPagerAdapter providesMainPagerAdapter(MainActivity activity) {
        return new MainPagerAdapter(activity.getSupportFragmentManager());
    }
}
