package com.footinit.base_mvvm.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.di.ApplicationContext;
import com.footinit.base_mvvm.di.PreferenceInfo;
import com.footinit.base_mvvm.utils.AppConstants;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Singleton
public class AppPreferenceHelper implements PreferenceHelper {

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";
    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";


    private SharedPreferences prefs;

    @Inject
    AppPreferenceHelper(@ApplicationContext Context context,
                        @PreferenceInfo String prefName) {
        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    @Override
    public void setCurrentUserId(Long userId) {
        long id = userId == null ? AppConstants.NULL_INDEX : userId;
        prefs.edit().putLong(PREF_KEY_CURRENT_USER_ID, id).apply();
    }

    @Override
    public Long getCurrentUserId() {
        long id = prefs.getLong(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INDEX);
        return id;
    }

    @Override
    public void setCurrentUserName(String userName) {
        prefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserName() {
        return prefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        prefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    @Override
    public String getCurrentUserEmail() {
        return prefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    @Override
    public void updateUserInfoInPrefs(Long userId, String userName, String userEmail, DataManager.LoggedInMode mode) {
        setCurrentUserId(userId);
        setCurrentUserName(userName);
        setCurrentUserEmail(userEmail);
        setCurrentUserLoggedInMode(mode);
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        prefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return prefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE, DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedOut() {
        updateUserInfoInPrefs(
                null,
                null,
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);
    }
}
