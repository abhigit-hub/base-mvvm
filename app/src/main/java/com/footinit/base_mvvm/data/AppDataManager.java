package com.footinit.base_mvvm.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.footinit.base_mvvm.data.db.DbHelper;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;
import com.footinit.base_mvvm.data.network.ApiHelper;
import com.footinit.base_mvvm.data.network.model.LoginRequest;
import com.footinit.base_mvvm.data.prefs.PreferenceHelper;
import com.footinit.base_mvvm.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Singleton
public class AppDataManager implements DataManager {


    private final Context context;
    private final DbHelper dbHelper;
    private final PreferenceHelper preferenceHelper;
    private final ApiHelper apiHelper;

    @Inject
    AppDataManager(@ApplicationContext Context context,
                   DbHelper dbHelper,
                   PreferenceHelper preferenceHelper,
                   ApiHelper apiHelper) {

        this.context = context;
        this.dbHelper = dbHelper;
        this.preferenceHelper = preferenceHelper;
        this.apiHelper = apiHelper;
    }


    //DB:USER
    @Override
    public Observable<Long> saveUser(User user) {
        return dbHelper.saveUser(user);
    }

    @Override
    public Observable<User> getCurrentUser() {
        return dbHelper.getCurrentUser();
    }

    @Override
    public Completable wipeUserData() {
        return dbHelper.wipeUserData();
    }



    //DB:BLOG
    @Override
    public Observable<Long> insertBlog(Blog blog) {
        return dbHelper.insertBlog(blog);
    }

    @Override
    public Observable<List<Long>> insertBlogList(List<Blog> blogList) {
        return dbHelper.insertBlogList(blogList);
    }

    @Override
    public LiveData<List<Blog>> getBlogList() {
        return dbHelper.getBlogList();
    }

    @Override
    public Observable<List<Blog>> getBlogListObservable() {
        return dbHelper.getBlogListObservable();
    }

    @Override
    public Observable<Long> getBlogRecordCount() {
        return dbHelper.getBlogRecordCount();
    }

    @Override
    public Completable wipeBlogData() {
        return dbHelper.wipeBlogData();
    }



    //DB:OPEN SOURCE
    @Override
    public Observable<Long> insertOpenSource(OpenSource openSource) {
        return dbHelper.insertOpenSource(openSource);
    }

    @Override
    public Observable<List<Long>> insertOpenSourceList(List<OpenSource> openSourceList) {
        return dbHelper.insertOpenSourceList(openSourceList);
    }

    @Override
    public LiveData<List<OpenSource>> getOpenSourceList() {
        return dbHelper.getOpenSourceList();
    }

    @Override
    public Observable<List<OpenSource>> getOpenSourceListObservable() {
        return dbHelper.getOpenSourceListObservable();
    }

    @Override
    public Observable<Long> getOpenSourceRecordCount() {
        return dbHelper.getOpenSourceRecordCount();
    }

    @Override
    public Completable wipeOpenSourceData() {
        return dbHelper.wipeOpenSourceData();
    }



    //API CALL:USER
    @Override
    public Observable<User> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return apiHelper.doServerLoginApiCall(request);
    }

    @Override
    public Observable<User> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return apiHelper.doGoogleLoginApiCall(request);
    }

    @Override
    public Observable<User> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        return apiHelper.doFacebookLoginApiCall(request);
    }



    //API CALL:BLOG
    @Override
    public Observable<List<Blog>> doBlogListApiCall() {
        return apiHelper.doBlogListApiCall();
    }



    //API CALL:OPEN SOURCE
    @Override
    public Observable<List<OpenSource>> doOpenSourceListCall() {
        return apiHelper.doOpenSourceListCall();
    }



    //SHARED PREF:USER
    @Override
    public void setCurrentUserId(Long id) {
        preferenceHelper.setCurrentUserId(id);
    }

    @Override
    public Long getCurrentUserId() {
        return preferenceHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserName(String userName) {
        preferenceHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserName() {
        return preferenceHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        preferenceHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserEmail() {
        return preferenceHelper.getCurrentUserEmail();
    }

    @Override
    public void updateUserInfoInPrefs(Long userId, String userName, String userEmail, LoggedInMode mode) {
        preferenceHelper.updateUserInfoInPrefs(userId, userName, userEmail, mode);
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        preferenceHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return preferenceHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedOut() {
        preferenceHelper.setCurrentUserLoggedOut();
    }
}
