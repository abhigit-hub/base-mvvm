package com.footinit.base_mvvm.data.db;


import android.arch.lifecycle.LiveData;

import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by Abhijit on 11-11-2017.
 */

@Singleton
public class AppDbHelper implements DbHelper {

    private AppDatabase appDatabase;

    @Inject
    AppDbHelper(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }


    //USER
    @Override
    public Observable<Long> saveUser(final User user) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return appDatabase.userDao().insertUser(user);
            }
        });
    }

    @Override
    public Observable<User> getCurrentUser() {
        return Observable.fromCallable(() -> appDatabase.userDao().getUser());
    }

    @Override
    public Completable wipeUserData() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDatabase.userDao().nukeUserTable();
            }
        });
    }


    //BLOG
    @Override
    public Observable<Long> insertBlog(final Blog blog) {
        return Observable.fromCallable(() -> appDatabase.blogDao().insertBlog(blog));
    }

    @Override
    public Observable<List<Long>> insertBlogList(List<Blog> blogList) {
        return Observable.fromCallable(() -> appDatabase.blogDao().insertBlogList(blogList));
    }

    @Override
    public LiveData<List<Blog>> getBlogList() {
        return appDatabase.blogDao().getBlogList();
    }

    @Override
    public Observable<List<Blog>> getBlogListObservable() {
        return Observable.fromCallable(() -> appDatabase.blogDao().getBlogListObservable());
    }

    @Override
    public Observable<Long> getBlogRecordCount() {
        return Observable.fromCallable(() -> appDatabase.blogDao().getRecordsCount());
    }

    @Override
    public Completable wipeBlogData() {
        return Completable.fromAction(() -> appDatabase.blogDao().nukeBlogTable());
    }


    //OPEN SOURCE
    @Override
    public Observable<Long> insertOpenSource(OpenSource openSource) {
        return Observable.fromCallable(() -> appDatabase.openSourceDao().insertOpenSource(openSource));
    }

    @Override
    public Observable<List<Long>> insertOpenSourceList(List<OpenSource> openSourceList) {
        return Observable.fromCallable(() -> appDatabase.openSourceDao().insertOpenSourceList(openSourceList));
    }

    @Override
    public LiveData<List<OpenSource>> getOpenSourceList() {
        return appDatabase.openSourceDao().getOpenSourceList();
    }

    @Override
    public Observable<List<OpenSource>> getOpenSourceListObservable() {
        return Observable.fromCallable(() -> appDatabase.openSourceDao().getOpenSourceListObservable());
    }

    @Override
    public Observable<Long> getOpenSourceRecordCount() {
        return Observable.fromCallable(() -> appDatabase.openSourceDao().getRecordsCount());
    }

    @Override
    public Completable wipeOpenSourceData() {
        return Completable.fromAction(() -> appDatabase.openSourceDao().nukeOpenSourceTable());
    }
}
