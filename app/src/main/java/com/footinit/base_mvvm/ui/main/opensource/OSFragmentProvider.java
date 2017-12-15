package com.footinit.base_mvvm.ui.main.opensource;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Abhijit on 09-12-2017.
 */

@Module
public abstract class OSFragmentProvider {

    @ContributesAndroidInjector(modules = OSFragmentModule.class)
    abstract OSFragment provideOpenSourceFragmentFactory();
}
