package com.footinit.base_mvvm.ui.main.opensource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.AppLogger;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
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
                            .flatMap(openSources -> Observable.concat(
                                    getDataManager().wipeOpenSourceData().subscribeOn(getSchedulerProvider().io()).toObservable(),
                                    getDataManager().insertOpenSourceList(openSources).subscribeOn(getSchedulerProvider().io()))
                                    .doOnError(throwable -> AppLogger.e(throwable, OSViewModel.class.getSimpleName())))
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(openSources -> {
                                hideLoading();
                                onPullToRefreshEvent(false);
                            }, throwable -> {
                                hideLoading();
                                onPullToRefreshEvent(false);
                                showSnackbarMessage(R.string.could_not_fetch_items);
                            })
            );
        } else {
            onPullToRefreshEvent(false);
            showSnackbarMessage(R.string.no_internet);
        }
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
