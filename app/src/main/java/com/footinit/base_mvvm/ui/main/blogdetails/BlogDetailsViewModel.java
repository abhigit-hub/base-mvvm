package com.footinit.base_mvvm.ui.main.blogdetails;

import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

/**
 * Created by Abhijit on 09-12-2017.
 */

public class BlogDetailsViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> returnToMainActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openInBrowserEvent = new SingleLiveEvent<>();


    public BlogDetailsViewModel(DataManager dataManager,
                                SchedulerProvider schedulerProvider,
                                NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }

    public SingleLiveEvent<Void> getReturnToMainActivityEvent() {
        return returnToMainActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenInBrowserEvent() {
        return openInBrowserEvent;
    }


    public void onBlogDetailsDisplayedError() {
        returnToMainActivityEvent.call();
    }

    public void onBlogFABClicked() {
        openInBrowserEvent.call();
    }
}
