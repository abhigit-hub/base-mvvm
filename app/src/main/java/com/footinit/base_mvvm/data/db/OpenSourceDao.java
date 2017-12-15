package com.footinit.base_mvvm.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.footinit.base_mvvm.data.db.model.OpenSource;

import java.util.List;

/**
 * Created by Abhijit on 23-11-2017.
 */

@Dao
public interface OpenSourceDao {


    @Insert
    Long insertOpenSource(OpenSource openSource);

    @Insert
    List<Long> insertOpenSourceList(List<OpenSource> openSourceList);

    @Query("SELECT * FROM opensource")
    List<OpenSource> getOpenSourceList();

    @Query("SELECT COUNT(id) FROM opensource")
    Long getRecordsCount();

    @Query("DELETE FROM opensource")
    void nukeOpenSourceTable();
}
