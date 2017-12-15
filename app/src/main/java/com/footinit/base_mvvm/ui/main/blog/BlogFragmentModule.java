package com.footinit.base_mvvm.ui.main.blog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.footinit.base_mvvm.ViewModelProviderFactory;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhijit on 08-12-2017.
 */

@Module
public class BlogFragmentModule {

    @Provides
    BlogViewModel providesBlogViewModel(DataManager dataManager,
                                        SchedulerProvider schedulerProvider,
                                        NetworkUtils networkUtils) {
        return new BlogViewModel(dataManager, schedulerProvider, networkUtils);
    }

    @Provides
    ViewModelProvider.Factory blogViewModelProvider(BlogViewModel blogViewModel) {
        return new ViewModelProviderFactory<>(blogViewModel);
    }

    @Provides
    BlogAdapter providesBlogAdapter() {
        return new BlogAdapter(new ArrayList<Blog>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(BlogFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }
}
