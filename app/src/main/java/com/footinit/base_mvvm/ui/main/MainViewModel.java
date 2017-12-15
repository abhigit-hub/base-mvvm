package com.footinit.base_mvvm.ui.main;

import com.facebook.login.LoginManager;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;
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

public class MainViewModel extends BaseViewModel {

    //EVENT (transmit DATA)
    private final SingleLiveEvent<Boolean> pullToRefreshEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<Blog>> blogList = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<OpenSource>> openSourceList = new SingleLiveEvent<>();
    private final SingleLiveEvent<User> user = new SingleLiveEvent<>();


    //EVENT
    private final SingleLiveEvent<Void> resetAllAdapterEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openLoginActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openFeedActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> closeNavigationDrawerEvent = new SingleLiveEvent<>();


    public MainViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }



    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */

    public SingleLiveEvent<Boolean> getPullToRefreshEvent() {
        return pullToRefreshEvent;
    }

    public SingleLiveEvent<List<Blog>> getBlogList() {
        if (blogList.getValue() == null) {
            blogList.setValue(new ArrayList<>());
        }
        return blogList;
    }

    public SingleLiveEvent<List<OpenSource>> getOpenSourceList() {
        if (openSourceList.getValue() == null) {
            openSourceList.setValue(new ArrayList<>());
        }
        return openSourceList;
    }

    public SingleLiveEvent<User> getUser() {
        return user;
    }

    public SingleLiveEvent<Void> getResetAllAdapterEvent() {
        return resetAllAdapterEvent;
    }

    public SingleLiveEvent<Void> getOpenLoginActivityEvent() {
        return openLoginActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenFeedActivityEvent() {
        return openFeedActivityEvent;
    }

    public SingleLiveEvent<Void> getCloseNavigationDrawerEvent() {
        return closeNavigationDrawerEvent;
    }



    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onPullToRefreshEvent(boolean status) {
        pullToRefreshEvent.setValue(status);
    }

    private void onBlogListUpdated(List<Blog> blogList) {
        if (this.blogList.getValue() != null) {
            this.blogList.getValue().clear();
            this.blogList.getValue().addAll(blogList);
            this.blogList.setValue(this.blogList.getValue());
        }
    }

    private void onOpenSourceListUpdated(List<OpenSource> openSourceList) {
        if (this.openSourceList.getValue() != null) {
            this.openSourceList.getValue().clear();
            this.openSourceList.getValue().addAll(openSourceList);
            this.openSourceList.setValue(this.openSourceList.getValue());
        }
    }

    private void onUserUpdated(User user) {
        this.user.setValue(user);
    }

    private void onResetAllAdapterEvent() {
        resetAllAdapterEvent.call();
    }

    private void onOpenLoginActivityEvent() {
        openLoginActivityEvent.call();
    }

    private void onOpenFeedActivityEvent() {
        openFeedActivityEvent.call();
    }

    private void onCloseNavigationDrawerEvent() {
        closeNavigationDrawerEvent.call();
    }


    public void onNavMenuCreated() {
        onUserUpdated(new User(
                getDataManager().getCurrentUserId(),
                getDataManager().getCurrentUserName(),
                getDataManager().getCurrentUserEmail()));
    }

    public void onDrawerOptionFeedClicked() {
        onCloseNavigationDrawerEvent();
        checkFeedAvailableInDb();
    }


    private void checkFeedAvailableInDb() {
        showLoading();

        getCompositeDisposable().add(
                getDataManager().getBlogRecordCount()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                if (aLong > 0) {
                                    hideLoading();
                                    onOpenFeedActivityEvent();
                                } else {
                                    hideLoading();
                                    checkFeedAvailableInOpenSourceDb();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hideLoading();
                                checkFeedAvailableInOpenSourceDb();
                            }
                        })
        );
    }

    private void checkFeedAvailableInOpenSourceDb() {
        showLoading();
        getCompositeDisposable().add(
                getDataManager().getOpenSourceRecordCount()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe((Consumer<Long>) aLong -> {
                            if (aLong > 0) {
                                hideLoading();
                                onOpenFeedActivityEvent();
                            } else {
                                hideLoading();
                                if (isInternet())
                                    showSnackbarMessage(R.string.something_went_wrong);
                            }
                        }, throwable -> {
                            hideLoading();
                            if (isInternet())
                                showSnackbarMessage(R.string.something_went_wrong);
                        })
        );
    }

    public void onDrawerOptionLogoutClicked() {
        showLoading();

    /*
    * Logout from the Facebook's LoginManager Instance
    * */
        LoginManager.getInstance().logOut();


    /*
    * Clearing Shared Preferences
    * */
        getDataManager().setCurrentUserLoggedOut();

    /*
    * Clearing/Wiping all data from the User Table
    * And if successful, logs User out
    * */

        getDataManager().wipeUserData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                        showToastMessage(R.string.logging_you_out);
                        onOpenLoginActivityEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showSnackbarMessage(R.string.there_was_an_error_logout);
                    }
                });
    }


    public void onRefreshNetworkCall() {
        if (isInternet()) {
            onPullToRefreshEvent(true);
            getCompositeDisposable().add(
                    getDataManager().doBlogListApiCall()
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(new Consumer<List<Blog>>() {
                                @Override
                                public void accept(List<Blog> blogList) throws Exception {
                                    if (blogList != null) {
                                        onBlogListUpdated(blogList);
                                        clearBlogListFromDb(blogList);
                                    }
                                    onOpenSourceNetworkCall();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    onPullToRefreshEvent(false);
                                    showSnackbarMessage(R.string.could_not_fetch_items);
                                }
                            })
            );
        } else {
            onPullToRefreshEvent(false);
        }
    }

    private void onOpenSourceNetworkCall() {
        onPullToRefreshEvent(true);
        getCompositeDisposable().add(
                getDataManager().doOpenSourceListCall()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(new Consumer<List<OpenSource>>() {
                            @Override
                            public void accept(List<OpenSource> list) throws Exception {
                                onPullToRefreshEvent(false);
                                if (list != null) {
                                    onOpenSourceListUpdated(list);
                                    clearOpenSourceListFromDb(list);
                                }
                                showToastMessage(R.string.updated_items);
                                onResetAllAdapterEvent();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                onPullToRefreshEvent(false);
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

    private void clearOpenSourceListFromDb(List<OpenSource> openSourceList) {

        getDataManager().wipeOpenSourceData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        addOpenSourceListToDb(openSourceList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void addOpenSourceListToDb(List<OpenSource> openSourceList) {
        getCompositeDisposable().add(
                getDataManager().insertOpenSourceList(openSourceList)
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
}
