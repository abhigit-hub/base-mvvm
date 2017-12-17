package com.footinit.base_mvvm.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.footinit.base_mvvm.data.db.model.Blog;

import java.util.List;

/**
 * Created by Abhijit on 23-11-2017.
 */

@Dao
public interface BlogDao {

    @Insert
    Long insertBlog(Blog blog);

    @Insert
    List<Long> insertBlogList(List<Blog> blogList);

    @Query("SELECT * FROM blog")
    LiveData<List<Blog>> getBlogList();

    @Query("SELECT * FROM blog")
    List<Blog> getBlogListObservable();

    @Query("SELECT COUNT(id) FROM blog")
    Long getRecordsCount();

    @Query("DELETE FROM blog")
    void nukeBlogTable();
}
