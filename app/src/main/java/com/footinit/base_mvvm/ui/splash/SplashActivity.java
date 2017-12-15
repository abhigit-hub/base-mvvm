package com.footinit.base_mvvm.ui.splash;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.ui.base.BaseActivity;
import com.footinit.base_mvvm.ui.login.LoginActivity;
import com.footinit.base_mvvm.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 04-12-2017.
 */

public class SplashActivity extends BaseActivity<SplashViewModel> {

    @Inject
    ViewModelProvider.Factory factory;


    @BindView(R.id.lottie_load)
    LottieAnimationView loadLAV;


    private SplashViewModel splashViewModel;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        setUnBinder(ButterKnife.bind(this));

        setUp();

        observeOpenLoginActivityEvent();

        observeOpenMainActivityEvent();
    }

    @Override
    public SplashViewModel getViewModel() {
        splashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel.class);
        return splashViewModel;
    }

    @Override
    protected void setUp() {
        loadLAV.playAnimation();
    }

    @Override
    protected void onDestroy() {
        loadLAV.clearAnimation();
        super.onDestroy();
    }


    private void observeOpenMainActivityEvent() {
        splashViewModel.getOpenMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openMainActivity();
                    }
                });
    }

    private void observeOpenLoginActivityEvent() {
        splashViewModel.getOpenLoginActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openLoginActivity();
                    }
                });
    }

    private void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(SplashActivity.this));
        finish();
    }

    private void openMainActivity() {
        startActivity(MainActivity.getStartIntent(SplashActivity.this));
        finish();
    }
}
