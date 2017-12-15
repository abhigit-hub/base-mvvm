package com.footinit.base_mvvm.data.network;


import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;
import com.footinit.base_mvvm.data.network.model.LoginRequest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by Abhijit on 10-11-2017.
 */

@Singleton
public class AppApiHelper implements ApiHelper{

    private ApiCall apiCall;

    @Inject
    AppApiHelper(ApiCall apiCall) {
        this.apiCall = apiCall;
    }


    @Override
    public Observable<User> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return apiCall.doServerLogin(request);
    }

    @Override
    public Observable<User> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return apiCall.doGoogleLogin(request);
    }

    @Override
    public Observable<User> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        return apiCall.doFacebookLogin(request);
    }

    @Override
    public Observable<List<Blog>> doBlogListApiCall() {
        return apiCall.getBlogList();
    }

    @Override
    public Observable<List<OpenSource>> doOpenSourceListCall() {
        return apiCall.getOpenSourceList();
    }
}
