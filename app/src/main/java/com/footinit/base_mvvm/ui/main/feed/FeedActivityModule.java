package com.footinit.base_mvvm.ui.main.feed;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 10-12-2017.
 */

@Module
public class FeedActivityModule {

    @Provides
    FeedViewModel providesFeedViewModel(DataManager dataManager,
                                        SchedulerProvider schedulerProvider,
                                        NetworkUtils networkUtils) {
        return new FeedViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory feedViewModelProvider(FeedViewModel feedViewModel) {
        return new ViewModelProviderFactory<>(feedViewModel);
    }

    @Provides
    LinearLayoutManager providesLinearLayoutManager(FeedActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    FeedAdapter providesFeedAdapter() {
        return new FeedAdapter(new ArrayList<Object>());
    }
}
