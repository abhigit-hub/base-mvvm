package com.footinit.base_mvvm.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
