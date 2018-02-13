package com.footinit.base_mvvm.data.db;

import android.arch.lifecycle.LiveData;

import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Abhijit on 11-11-2017.
 */

public interface DbHelper {

    //User
    Observable<Long> saveUser(User user);

    Observable<User> getCurrentUser();

    Completable wipeUserData();


    //Blog
    Observable<Long> insertBlog(Blog blog);

    Observable<List<Long>> insertBlogList(List<Blog> blogList);

    LiveData<List<Blog>> getBlogList();

    Observable<List<Blog>> getBlogListObservable();

    Observable<Long> getBlogRecordCount();

    Completable wipeBlogData();


    //Open Source
    Observable<Long> insertOpenSource(OpenSource openSource);

    Observable<List<Long>> insertOpenSourceList(List<OpenSource> openSourceList);

    LiveData<List<OpenSource>> getOpenSourceList();

    Observable<List<OpenSource>> getOpenSourceListObservable();

    Observable<Long> getOpenSourceRecordCount();

    Completable wipeOpenSourceData();

}
