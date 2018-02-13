package com.footinit.base_mvvm.ui.login;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.model.User;
import com.footinit.base_mvvm.data.network.model.LoginRequest;
import com.footinit.base_mvvm.ui.base.BaseViewModel;
import com.footinit.base_mvvm.utils.CommonUtils;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.interactors.SingleLiveEvent;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;


import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Abhijit on 07-12-2017.
 */

public class LoginViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> openMainActivityEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> openGoogleSignInActivityEvent = new SingleLiveEvent<>();


    public LoginViewModel(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          NetworkUtils networkUtils) {
        super(dataManager, schedulerProvider, networkUtils);
    }


    /*
    * NAVIGATION
    * GETTERS for observing events from UI thread(i.e Activity)
    * */

    public SingleLiveEvent<Void> getOpenMainActivityEvent() {
        return openMainActivityEvent;
    }

    public SingleLiveEvent<Void> getOpenGoogleSignInActivityEvent() {
        return openGoogleSignInActivityEvent;
    }

    /*
    * NAVIGATION
    * Commands to update Events, which are observed from UI thread
    * */

    private void onOpenMainActivityEvent() {
        openMainActivityEvent.call();
    }

    private void onOpenGoogleSignInActivityEvent() {
        openGoogleSignInActivityEvent.call();
    }


    //SERVER
    /*
    *
    * Login from Google and Facebook has negative id's for identification, Server has positive ID's
    *
    * Based on the ID's, we can deduce the form of Login
    * */
    public void onServerLoginClicked(String email, String password) {


        if (isInternet()) {
            if (email == null || email.isEmpty()) {
                showSnackbarMessage(R.string.empty_email);
                return;
            }
            if (!CommonUtils.isEmailValid(email)) {
                showSnackbarMessage(R.string.invalid_email);
                return;
            }
            if (password == null || password.isEmpty()) {
                showSnackbarMessage(R.string.empty_password);
                return;
            }


            showLoading();
            getCompositeDisposable().add(
                    getDataManager().doServerLoginApiCall(new LoginRequest.ServerLoginRequest(email, password))
                            .concatMap(user -> getDataManager().saveUser(user)
                                    .ignoreElements()
                                    .andThen(Observable.just(user)))
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(user -> {
                                getDataManager().updateUserInfoInPrefs(user.getUserID(),
                                        user.getUserName(),
                                        user.getEmail(),
                                        DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_SERVER);

                                hideLoading();
                                showToastMessage(R.string.signing_in);
                                onOpenMainActivityEvent();
                            }, throwable -> {
                                hideLoading();
                                showToastMessage(R.string.server_sign_in_failed);
                            })
            );
        }
    }


    //GOOGLE
    public void onGoogleLoginClicked() {
        if (isInternet())
            onOpenGoogleSignInActivityEvent();
    }

    public void onGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            onGoogleLoginSuccessful(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Timber.d("signInResult:failed code=" + e.getStatusCode());
            showSnackbarMessage(R.string.google_sign_in_failed);
        }
    }

    /*
    *
    * Login from Google and Facebook has negative id's for identification, Server has positive ID's
    *
    * Based on the ID's, we can deduce the form of Login
    * */
    private void onGoogleLoginSuccessful(GoogleSignInAccount account) {
        showToastMessage(R.string.google_sign_in_successful);

        long id = CommonUtils.getNegativeLong(DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_GOOGLE.getType());
        String name = account.getDisplayName();
        String email = account.getEmail();

        insertCurrentUserIntoDb(new User(id, name, email));

        getDataManager().updateUserInfoInPrefs(
                id,
                name,
                email,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_GOOGLE);
    }


    //FACEBOOK
    public void onFacebookSignInResult(AccessToken accessToken,
                                       Profile currentProfile) {
        if (isInternet()) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            onFacebookLoginSuccessful(currentProfile, object);
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    /*
    *
    * Login from Google and Facebook has negative id's for identification, Server has positive ID's
    *
    * Based on the ID's, we can deduce the form of Login
    * */
    private void onFacebookLoginSuccessful(Profile profile, JSONObject object) {
        showToastMessage(R.string.facebook_sign_in_successful);

        String email = object.optString("email");
        if (email == null) email = " ";
        long id = CommonUtils.getNegativeLong(DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_FB.getType());
        String name = profile.getName();

        insertCurrentUserIntoDb(new User(id, name, email));

        getDataManager().updateUserInfoInPrefs(
                id,
                name,
                email,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_FB);
    }


    //DB OPERATION
    private void insertCurrentUserIntoDb(User user) {
        showLoading();

        getCompositeDisposable().add(
                getDataManager().saveUser(user)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(aLong -> {
                            hideLoading();
                            onOpenMainActivityEvent();
                        }, throwable -> {
                            showLoading();
                            showSnackbarMessage(R.string.cannnot_initiate_sign_in);
                        })
        );
    }
}
