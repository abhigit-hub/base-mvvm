package com.footinit.base_mvvm.ui.main.opensource;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 08-12-2017.
 */

@Module
public class OSFragmentModule {


    @Provides
    OSViewModel providesOSViewModel(DataManager dataManager,
                                    SchedulerProvider schedulerProvider,
                                    NetworkUtils networkUtils) {
        return new OSViewModel(dataManager, schedulerProvider, networkUtils);
    }


    @Provides
    ViewModelProvider.Factory OSViewModelProvider(OSViewModel osViewModel) {
        return new ViewModelProviderFactory<>(osViewModel);
    }

    @Provides
    OSAdapter provideOpenSourceAdapter() {
        return new OSAdapter(new ArrayList<OpenSource>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(OSFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }
}
