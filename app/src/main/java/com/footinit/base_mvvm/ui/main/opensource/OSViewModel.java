package com.footinit.base_mvvm.ui.main.opensource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.OpenSource;
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

public class OSViewModel extends BaseViewModel implements OSAdapter.Callback {

    //EVENT (transmit DATA)
    private LiveData<List<OpenSource>> openSourceList = new MutableLiveData<>();
    private final SingleLiveEvent<OpenSource> openOSDetailActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> pullToRefreshEvent = new SingleLiveEvent<>();
    //EVENT
    private final SingleLiveEvent<Void> openSourceListReFetched = new SingleLiveEvent<>();


    public OSViewModel(DataManager dataManager,
                       SchedulerProvider schedulerProvider,
                       NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        openSourceList = getDataManager().getOpenSourceList();
    }


    /*
* NAVIGATION
* GETTERS for observing events from UI thread(i.e Activity)
* */
    public LiveData<List<OpenSource>> getOpenSourceList() {
        return openSourceList;
    }

    public SingleLiveEvent<OpenSource> getOpenOSDetailActivityEvent() {
        return openOSDetailActivityEvent;
    }

    public SingleLiveEvent<Boolean> getPullToRefreshEvent() {
        return pullToRefreshEvent;
    }

    public SingleLiveEvent<Void> getOpenSourceListReFetched() {
        return openSourceListReFetched;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenOSDetailActivity(OpenSource openSource) {
        openOSDetailActivityEvent.setValue(openSource);
    }

    private void onPullToRefreshEvent(boolean status) {
        pullToRefreshEvent.setValue(status);
    }

    private void onOSListReFetched() {
        openSourceListReFetched.call();
    }


    public void fetchOpenSourceList() {
        if (isInternet()) {
            showLoading();
            onPullToRefreshEvent(true);
            getCompositeDisposable().add(
                    getDataManager().doOpenSourceListCall()
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(new Consumer<List<OpenSource>>() {
                                @Override
                                public void accept(List<OpenSource> list) throws Exception {
                                    hideLoading();
                                    onPullToRefreshEvent(false);
                                    if (list != null) {
                                        clearOSListFromDb(list);
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
            showSnackbarMessage(R.string.no_internet);
        }
    }


    private void clearOSListFromDb(List<OpenSource> openSourceList) {

        getDataManager().wipeOpenSourceData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        addOSListToDb(openSourceList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void addOSListToDb(List<OpenSource> openSourceList) {
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


    @Override
    public void onOpenSourceEmptyRetryClicked() {
        fetchOpenSourceList();
        onOSListReFetched();
    }

    @Override
    public void onOpenSourceItemClicked(OpenSource openSource) {
        onOpenOSDetailActivity(openSource);
    }
}
