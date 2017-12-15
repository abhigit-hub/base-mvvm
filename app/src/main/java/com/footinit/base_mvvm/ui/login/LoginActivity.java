package com.footinit.base_mvvm.ui.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.footinit.base_mvvm.MvvmApp;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.ui.base.BaseActivity;
import com.footinit.base_mvvm.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhijit on 07-12-2017.
 */

public class LoginActivity extends BaseActivity<LoginViewModel> {

    private static final int RC_GOOGLE_SIGN_IN = 1;
    private static final String EMAIL = "email";

    private LoginViewModel loginViewModel;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;


    @Inject
    ViewModelProvider.Factory factory;


    @BindView(R.id.et_email)
    EditText emailET;

    @BindView(R.id.et_password)
    EditText passwordET;

    @BindView(R.id.btn_fb_login)
    LoginButton btnFBLogin;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        setUnBinder(ButterKnife.bind(this));

        callbackManager = ((MvvmApp) getApplication()).getAppComponent().getCallbackManager();

        setUp();

        observeOpenMainActivityEvent();

        observeOpenGoogleSignInActivityEvent();
    }

    @OnClick(R.id.btn_server_login)
    void onServerLoginClicked() {
        loginViewModel.onServerLoginClicked(emailET.getText().toString(),
                passwordET.getText().toString());
    }

    @OnClick(R.id.btn_google_login)
    void onGoogleLoginClicked() {
        loginViewModel.onGoogleLoginClicked();
    }

    @Override
    protected void setUp() {
        btnFBLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();

                        /*
                        * Check if Profile is available, if not register for profile information
                        * */
                        if (Profile.getCurrentProfile() == null) {
                            profileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                    loginViewModel.onFacebookSignInResult(accessToken,
                                            Profile.getCurrentProfile());
                                    profileTracker.stopTracking();
                                }
                            };
                        } else {
                            loginViewModel.onFacebookSignInResult(accessToken,
                                    Profile.getCurrentProfile());
                        }

                    }

                    @Override
                    public void onCancel() {
                        showToast("Facebook Sign in Cancelled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        showToast("Facebook Sign in Failed");
                    }
                });
    }


    private void observeOpenMainActivityEvent() {
        loginViewModel.getOpenMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openMainActivity();
                    }
                });
    }

    private void observeOpenGoogleSignInActivityEvent() {
        loginViewModel.getOpenGoogleSignInActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openGoogleSignInActivity();
                    }
                });
    }



    public void openMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    public void openGoogleSignInActivity() {
        Intent intent = ((MvvmApp) getApplication()).getAppComponent().getGoogleSignInClient().getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginViewModel.onGoogleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public LoginViewModel getViewModel() {
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        return loginViewModel;
    }

    @Override
    protected void onDestroy() {
        btnFBLogin.registerCallback(callbackManager, null);
        if (profileTracker != null)
            profileTracker.stopTracking();
        super.onDestroy();
    }
}
