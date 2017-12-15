package com.footinit.base_mvvm.ui.base;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;


public abstract class BaseFragment<V extends BaseViewModel> extends Fragment {

    private BaseActivity activity;
    private Unbinder unBinder;

    private V viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();
        setUpSnackbar();
        setUpToast();
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    private void setUpSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer snackbarMessage) {
                        if (activity != null)
                            activity.showSnackbar(getString(snackbarMessage));
                    }
                });
    }

    private void setUpToast() {
        viewModel.getToastMessage().observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer toastMessage) {
                        if (activity != null)
                            activity.showToast(getString(toastMessage));
                    }
                });
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    public boolean isNetworkConnected() {
        return activity != null && activity.isNetworkConnected();
    }

    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    protected abstract void setUp(View view);


    @Override
    public void onDestroy() {
        if (unBinder != null)
            unBinder.unbind();
        super.onDestroy();
    }
}
