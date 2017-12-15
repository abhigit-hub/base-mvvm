package com.footinit.base_mvvm.ui.main.blog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 09-12-2017.
 */

@Module
public abstract class BlogFragmentProvider {

    @ContributesAndroidInjector(modules = BlogFragmentModule.class)
    abstract BlogFragment providesBlogFragmentFactory();
}
