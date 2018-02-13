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

import io.reactivex.Observable;

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
        retrieveAllList();
    }

    private void retrieveAllList() {
        showLoading();

        getCompositeDisposable().add(
                Observable.zip(getDataManager().getBlogListObservable(),
                        getDataManager().getOpenSourceListObservable(),
                        (t1, t2) -> {
                            List<Object> list = new ArrayList<>();

                            if (t1 != null && t1.size() > 0) list.addAll(t1);
                            if (t2 != null && t2.size() > 0) list.addAll(t2);
                            return list;
                        })
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(objectList -> {
                            hideLoading();
                            if (objectList != null && objectList.size() > 0) {
                                Collections.shuffle(objectList);
                                onUpdateList(objectList);
                            }
                        }, throwable -> {
                            hideLoading();
                            showSnackbarMessage(R.string.something_went_wrong);
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
