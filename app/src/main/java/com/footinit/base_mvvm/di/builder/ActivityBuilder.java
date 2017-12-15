package com.footinit.base_mvvm.di.builder;

import com.footinit.base_mvvm.ui.login.LoginActivity;
import com.footinit.base_mvvm.ui.login.LoginActivityModule;
import com.footinit.base_mvvm.ui.main.MainActivity;
import com.footinit.base_mvvm.ui.main.MainActivityModule;
import com.footinit.base_mvvm.ui.main.blog.BlogFragmentProvider;
import com.footinit.base_mvvm.ui.main.blogdetails.BlogDetailsActivity;
import com.footinit.base_mvvm.ui.main.blogdetails.BlogDetailsActivityModule;
import com.footinit.base_mvvm.ui.main.feed.FeedActivity;
import com.footinit.base_mvvm.ui.main.feed.FeedActivityModule;
import com.footinit.base_mvvm.ui.main.opensource.OSFragmentProvider;
import com.footinit.base_mvvm.ui.main.opensourcedetails.OSDetailsActivity;
import com.footinit.base_mvvm.ui.main.opensourcedetails.OSDetailsActivityModule;
import com.footinit.base_mvvm.ui.splash.SplashActivity;
import com.footinit.base_mvvm.ui.splash.SplashActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 04-12-2017.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = {
            MainActivityModule.class,
            BlogFragmentProvider.class,
            OSFragmentProvider.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = BlogDetailsActivityModule.class)
    abstract BlogDetailsActivity bindBlogDetailsActivity();

    @ContributesAndroidInjector(modules = OSDetailsActivityModule.class)
    abstract OSDetailsActivity bindOSDetailsActivity();

    @ContributesAndroidInjector(modules = FeedActivityModule.class)
    abstract FeedActivity bindFeedActivity();
}