package com.footinit.base_mvvm.ui.base;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.ProgressDialogStatus;
import com.footinit.base_mvvm.utils.interactors.SnackbarMessage;
import com.footinit.base_mvvm.utils.interactors.ToastMessage;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;


import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseViewModel extends ViewModel {

    private NetworkUtils networkUtils;

    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private final ToastMessage toastMessage = new ToastMessage();
    private final ProgressDialogStatus progressDialogStatus = new ProgressDialogStatus();

    private final DataManager dataManager;
    private final SchedulerProvider schedulerProvider;

    private CompositeDisposable compositeDisposable;

    public BaseViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         NetworkUtils networkUtils) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
        this.networkUtils = networkUtils;
    }


    public DataManager getDataManager() {
        return dataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public ToastMessage getToastMessage() {
        return toastMessage;
    }

    public ProgressDialogStatus getProgressDialogStatus() {
        return progressDialogStatus;
    }

    public void showSnackbarMessage(@StringRes Integer message) {
        snackbarMessage.setValue(message);
    }

    public void showToastMessage(@StringRes Integer message) {
        toastMessage.setValue(message);
    }

    public boolean isInternet() {
        if (networkUtils != null && networkUtils.isNetworkConnected())
            return true;

        showSnackbarMessage(R.string.no_internet);
        return false;
    }

    public void showLoading() {
        progressDialogStatus.setValue(true);
    }

    public void hideLoading() {
        progressDialogStatus.setValue(false);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
