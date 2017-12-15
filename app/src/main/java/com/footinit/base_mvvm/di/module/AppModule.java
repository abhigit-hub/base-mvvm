package com.footinit.base_mvvm.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.facebook.CallbackManager;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.AppDataManager;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.data.db.AppDatabase;
import com.footinit.base_mvvm.data.db.AppDbHelper;
import com.footinit.base_mvvm.data.db.DbHelper;
import com.footinit.base_mvvm.data.network.ApiCall;
import com.footinit.base_mvvm.data.network.ApiHelper;
import com.footinit.base_mvvm.data.network.AppApiHelper;
import com.footinit.base_mvvm.data.prefs.AppPreferenceHelper;
import com.footinit.base_mvvm.data.prefs.PreferenceHelper;
import com.footinit.base_mvvm.di.ApplicationContext;
import com.footinit.base_mvvm.di.DatabaseInfo;
import com.footinit.base_mvvm.di.PreferenceInfo;
import com.footinit.base_mvvm.utils.AppConstants;
import com.footinit.base_mvvm.utils.NetworkUtils;
import com.footinit.base_mvvm.utils.rx.AppSchedulerProvider;
import com.footinit.base_mvvm.utils.rx.SchedulerProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Module
public class AppModule {

    @Provides
    @ApplicationContext
    Context providesContext(Application application) {
        return application;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @DatabaseInfo
    String providesDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer providesDatabaseVersion() {
        return AppConstants.DB_VERSION;
    }

    @Provides
    @PreferenceInfo
    String providesSharedPrefName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager providesDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper providesDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    ApiHelper providesApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    PreferenceHelper providesPreferenceHelper(AppPreferenceHelper appPreferenceHelper) {
        return appPreferenceHelper;
    }

    @Provides
    @Singleton
    ApiCall providesApiCall() {
        return ApiCall.Factory.create();
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(@ApplicationContext Context context,
                                    @DatabaseInfo String dbName) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                dbName).build();
    }

    @Provides
    @Singleton
    CalligraphyConfig providesCalligraphyConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    GoogleSignInOptions providesGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    @Provides
    @Singleton
    GoogleSignInClient providesGoogleSignInClient(GoogleSignInOptions googleSignInOptions,
                                                  @ApplicationContext Context context) {
        return GoogleSignIn.getClient(context, googleSignInOptions);
    }

    @Provides
    @Singleton
    CallbackManager providesCallbackManager() {
        return CallbackManager.Factory.create();
    }

    @Provides
    @Singleton
    NetworkUtils providesNetworkUtils(@ApplicationContext Context context) {
        return new NetworkUtils(context);
    }
}
