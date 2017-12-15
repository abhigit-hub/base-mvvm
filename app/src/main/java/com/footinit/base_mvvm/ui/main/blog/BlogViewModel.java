package com.footinit.base_mvvm.ui.main.blog;

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
    private final SingleLiveEvent<List<Blog>> blogList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Blog> openBlogDetailActivity = new SingleLiveEvent<>();
    //EVENT
    private final SingleLiveEvent<Void> blogListReFetched = new SingleLiveEvent<>();


    public BlogViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */
    public SingleLiveEvent<List<Blog>> getBlogList() {
        if (blogList.getValue() == null) {
            blogList.setValue(new ArrayList<>());
        }
        return blogList;
    }

    public SingleLiveEvent<Blog> getOpenBlogDetailActivity() {
        return openBlogDetailActivity;
    }

    public SingleLiveEvent<Void> getBlogListReFetched() {
        return blogListReFetched;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */
    public void onUpdateBlogList(List<Blog> blogList) {
        if (this.blogList.getValue() != null) {
            this.blogList.getValue().clear();
            this.blogList.getValue().addAll(blogList);
            this.blogList.setValue(this.blogList.getValue());
        }
    }

    public void onAddToBlogList(List<Blog> blogList) {
        if (this.blogList.getValue() != null) {
            this.blogList.getValue().addAll(blogList);
            this.blogList.setValue(this.blogList.getValue());
        }
    }

    private void onOpenBlogDetailActivity(Blog blog) {
        openBlogDetailActivity.setValue(blog);
    }

    private void onBlogListReFetched() {
        blogListReFetched.call();
    }



    public void fetchBlogList() {
        if (isInternet()) {
            showLoading();
            getCompositeDisposable().add(
                    getDataManager().doBlogListApiCall()
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(new Consumer<List<Blog>>() {
                                @Override
                                public void accept(List<Blog> blogList) throws Exception {
                                    hideLoading();
                                    if (blogList != null) {
                                        onAddToBlogList(blogList);
                                        clearBlogListFromDb(blogList);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    hideLoading();
                                    showSnackbarMessage(R.string.could_not_fetch_items);
                                    showPersistentData();
                                }
                            })
            );
        } else {
            showPersistentData();
        }
    }


    private void showPersistentData() {
        getCompositeDisposable().add(
                getDataManager().getBlogList()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<List<Blog>>() {
                            @Override
                            public void accept(List<Blog> blogList) throws Exception {
                                if (blogList != null) {
                                    onAddToBlogList(blogList);
                                }
                                showSnackbarMessage(R.string.showing_stale_items);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showSnackbarMessage(R.string.could_not_fetch_items);
                            }
                        })
        );
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
