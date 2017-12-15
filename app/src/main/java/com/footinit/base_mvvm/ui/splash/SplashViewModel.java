package com.footinit.base_mvvm.ui.splash;

import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Abhijit on 04-12-2017.
 */

public class SplashViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> openLoginActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openMainActivityEvent = new SingleLiveEvent<>();

    public SplashViewModel(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);

        startActivityWithDelay();
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */

    public SingleLiveEvent<Void> getOpenLoginActivityEvent() {
        return openLoginActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenMainActivityEvent() {
        return openMainActivityEvent;
    }


    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenLoginActivityEvent() {
        openLoginActivityEvent.call();
    }

    private void onOpenMainActivityEvent() {
        openMainActivityEvent.call();
    }



    /*
    * APP LOGIC, BUSINESS LOGIC & USE CASES
    * */

    private void startActivityWithDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                decideNextActivity();
            }
        }, 2000);
    }

    private void decideNextActivity() {
        if (getDataManager().getCurrentUserLoggedInMode() ==
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())
            onOpenLoginActivityEvent();
        else onOpenMainActivityEvent();
    }
}
