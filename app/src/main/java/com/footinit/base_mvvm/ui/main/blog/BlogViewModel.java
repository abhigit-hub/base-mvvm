package com.footinit.base_mvvm.ui.main.blog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class BlogViewModel extends BaseViewModel implements BlogAdapter.Callback {

    //EVENT (transmit DATA)
    private LiveData<List<Blog>> blogList = new MutableLiveData<>();
    private final SingleLiveEvent<Blog> openBlogDetailActivity = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> pullToRefreshEvent = new SingleLiveEvent<>();

    //EVENT
    private final SingleLiveEvent<Void> blogListReFetched = new SingleLiveEvent<>();


    public BlogViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        blogList = getDataManager().getBlogList();
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */
    public LiveData<List<Blog>> getBlogList() {
        return blogList;
    }

    public SingleLiveEvent<Blog> getOpenBlogDetailActivity() {
        return openBlogDetailActivity;
    }

    public SingleLiveEvent<Boolean> getPullToRefreshEvent() {
        return pullToRefreshEvent;
    }

    public SingleLiveEvent<Void> getBlogListReFetched() {
        return blogListReFetched;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenBlogDetailActivity(Blog blog) {
        openBlogDetailActivity.setValue(blog);
    }

    private void onPullToRefreshEvent(boolean status) {
        pullToRefreshEvent.setValue(status);
    }

    private void onBlogListReFetched() {
        blogListReFetched.call();
    }



    public void fetchBlogList() {
        if (isInternet()) {
            showLoading();
            onPullToRefreshEvent(true);
            getCompositeDisposable().add(
                    getDataManager().doBlogListApiCall()
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(new Consumer<List<Blog>>() {
                                @Override
                                public void accept(List<Blog> blogList) throws Exception {
                                    hideLoading();
                                    onPullToRefreshEvent(false);
                                    if (blogList != null) {
                                        clearBlogListFromDb(blogList);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    hideLoading();
                                    onPullToRefreshEvent(false);
                                    showSnackbarMessage(R.string.could_not_fetch_items);
                                }
                            })
            );
        } else {
            onPullToRefreshEvent(false);
            showSnackbarMessage(R.string.no_internet);
        }
    }


    private void clearBlogListFromDb(List<Blog> blogList) {

        getDataManager().wipeBlogData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        addBlogListToDb(blogList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void addBlogListToDb(List<Blog> blogList) {
        getCompositeDisposable().add(
                getDataManager().insertBlogList(blogList)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<List<Long>>() {
                            @Override
                            public void accept(List<Long> longs) throws Exception {
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        })
        );
    }

    @Override
    public void onBlogEmptyRetryClicked() {
        fetchBlogList();
        onBlogListReFetched();
    }

    @Override
    public void onBlogItemClicked(Blog blog) {
        onOpenBlogDetailActivity(blog);
    }
}
