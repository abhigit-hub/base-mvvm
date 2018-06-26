package com.footinit.base_mvvm.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;

import javax.inject.Singleton;

/**
 * Created by Abhijit on 11-11-2017.
 */

@Database(entities = {User.class, Blog.class, OpenSource.class}, version = 1, exportSchema = false)
@Singleton
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract BlogDao blogDao();

    public abstract OpenSourceDao openSourceDao();
}
