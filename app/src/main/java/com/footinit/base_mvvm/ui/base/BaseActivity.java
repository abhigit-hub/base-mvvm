package com.footinit.base_mvvm.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.utils.CommonUtils;
import com.footinit.base_mvvm.utils.NetworkUtils;

import javax.inject.Inject;

import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity
        implements BaseFragment.Callback {

    @Inject
    NetworkUtils networkUtils;

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog progressDialog;
    private Unbinder unBinder;

    private V viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDependencyInjection();
        setUpViewModel();
        setUpSnackbar();
        setUpToast();
        setUpProgressDialog();
    }

    private void setUpViewModel() {
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
    }

    private void setUpSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer snackbarMessage) {
                        showSnackbar(getString(snackbarMessage));
                    }
                });
    }

    private void setUpToast() {
        viewModel.getToastMessage().observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer toastMessage) {
                        showToast(getString(toastMessage));
                    }
                });
    }

    private void setUpProgressDialog() {
        viewModel.getProgressDialogStatus().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean status) {
                        if (status)
                            BaseActivity.this.showLoading();
                        else BaseActivity.this.hideLoading();
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isNetworkConnected() {
        if (networkUtils.isNetworkConnected())
            return true;
        else {
            showSnackbar(getString(R.string.no_internet));
            return false;
        }
    }

    public void showLoading() {
        hideLoading();
        progressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    protected void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT);

        View view = snackbar.getView();

        TextView snackTV = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        snackTV.setTextColor(ContextCompat.getColor(this, R.color.white));

        snackbar.show();
    }

    protected void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    protected abstract void setUp();

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void onDestroy() {
        if (unBinder != null)
            unBinder.unbind();
        super.onDestroy();
    }
}

