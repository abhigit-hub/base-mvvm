package com.footinit.base_mvvm.di.component;

import android.app.Application;

import com.facebook.CallbackManager;
import com.footinit.base_mvvm.MvvmApp;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.di.builder.ActivityBuilderModule;
import com.footinit.base_mvvm.di.builder.ServiceBuilderModule;
import com.footinit.base_mvvm.di.module.AppModule;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Abhijit on 04-12-2017.
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class,
        ActivityBuilderModule.class, ServiceBuilderModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(MvvmApp mvvmApp);

    DataManager getDataManager();

    GoogleSignInClient getGoogleSignInClient();

    CallbackManager getCallbackManager();
}
