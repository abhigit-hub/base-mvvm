package com.footinit.base_mvvm.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhijit on 11-11-2017.
 */

@Entity(tableName = "user")
public class User {

    /*NOTE:
    @ColumnInfo(name = "id")
    This is a form of mapping.

    If API response has key as "id", no need to have @ColumnInfo annotation.

     If API response has key anything apart from "id" (example "ID", "Identifier", "Id")in its body,
     then @ColumnInfo annotation has to be given for mapping and parsing correctly.

    * */

    public User(long userID, String userName, String email) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
    }

    @Ignore
    @Expose @SerializedName("status_code")
    private String statusCode;

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @Expose @SerializedName("user_id")
    private long userID;

    @ColumnInfo(name = "user_name")
    @Expose @SerializedName("user_name")
    private String userName;

    @ColumnInfo(name = "email")
    @Expose @SerializedName("email")
    private String email;

    @ColumnInfo(name = "access_token")
    @Expose @SerializedName("access_token")
    private String accessToken;

    @Ignore
    private String message;



    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
