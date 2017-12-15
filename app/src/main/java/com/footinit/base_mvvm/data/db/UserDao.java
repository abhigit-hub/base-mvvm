package com.footinit.base_mvvm.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.footinit.base_mvvm.data.db.model.User;


/**
 * Created by Abhijit on 11-11-2017.
 */

@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Query("SELECT * FROM user")
    User getUser();

    @Query("DELETE FROM user")
    void nukeUserTable();
}
