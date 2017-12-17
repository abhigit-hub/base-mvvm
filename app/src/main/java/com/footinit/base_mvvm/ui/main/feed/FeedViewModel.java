package com.footinit.base_mvvm.ui.main.feed;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Abhijit on 10-12-2017.
 */

public class FeedViewModel extends BaseViewModel
        implements FeedAdapter.Callback {

    private final SingleLiveEvent<Blog> openBlogDetailsActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<OpenSource> openOSDetailsActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<Object>> list = new SingleLiveEvent<>();


    public FeedViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }


    public SingleLiveEvent<Blog> getOpenBlogDetailsActivityEvent() {
        return openBlogDetailsActivityEvent;
    }

    public SingleLiveEvent<OpenSource> getOpenOSDetailsActivityEvent() {
        return openOSDetailsActivityEvent;
    }

    public SingleLiveEvent<List<Object>> getList() {
        if (list.getValue() == null) {
            list.setValue(new ArrayList<>());
        }
        return list;
    }

    private void openBlogDetailsActivity(Blog blog) {
        openBlogDetailsActivityEvent.setValue(blog);
    }

    private void openOSDetailsActivity(OpenSource openSource) {
        openOSDetailsActivityEvent.setValue(openSource);
    }

    public void onUpdateList(List<Object> objectList) {
        if (this.list.getValue() != null) {
            this.list.getValue().clear();
            this.list.getValue().addAll(objectList);
            this.list.setValue(this.list.getValue());
        }
    }

    public void onViewPrepared() {
        retrieveBlogList();
    }

    private void retrieveBlogList() {
        showLoading();
        getCompositeDisposable().add(
                getDataManager().getBlogListObservable()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogList -> {
                            hideLoading();
                            if (blogList != null) {
                                ArrayList<Object> list = new ArrayList<>();
                                list.addAll(blogList);
                                retrieveOpenSourceList(list);
                            }

                        }, throwable -> {
                            hideLoading();
                            retrieveOpenSourceList(new ArrayList<Object>());
                        })
        );
    }

    private void retrieveOpenSourceList(List<Object> list) {
        showLoading();
        getCompositeDisposable().add(
                getDataManager().getOpenSourceListObservable()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(openSourceList -> {
                            hideLoading();
                            if (openSourceList != null) {
                                list.addAll(openSourceList);
                                Collections.shuffle(list);
                                onUpdateList(list);
                            }
                        }, throwable -> {
                            hideLoading();
                            showSnackbarMessage(R.string.something_went_wrong);

                            if (list != null && list.size() > 0)
                                onUpdateList(list);
                        })
        );
    }


    @Override
    public void onBlogItemClicked(Blog blog) {
        openBlogDetailsActivity(blog);
    }

    @Override
    public void onOpenSourceItemClicked(OpenSource openSource) {
        openOSDetailsActivity(openSource);
    }
}
