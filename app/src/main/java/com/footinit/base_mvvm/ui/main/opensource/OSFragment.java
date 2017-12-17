package com.footinit.base_mvvm.ui.main.opensource;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseFragment;
import com.footinit.base_mvvm.ui.main.Interactor;
import com.footinit.base_mvvm.ui.main.opensourcedetails.OSDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class OSFragment extends BaseFragment<OSViewModel> {

    private Interactor.OpenSource callback;

    private OSViewModel osViewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    OSAdapter osAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;


    @BindView(R.id.rv_open_source)
    RecyclerView rvOpenSource;


    public static OSFragment newInstance() {
        Bundle args = new Bundle();
        OSFragment fragment = new OSFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_open_source, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        osAdapter.setCallback(osViewModel);

        observeAllEvents();

        return view;
    }

    @Override
    public OSViewModel getViewModel() {
        osViewModel = ViewModelProviders.of(this, factory).get(OSViewModel.class);
        return osViewModel;
    }

    @Override
    protected void setUp(View view) {
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvOpenSource.setLayoutManager(linearLayoutManager);
        rvOpenSource.setItemAnimator(new DefaultItemAnimator());
        rvOpenSource.setAdapter(osAdapter);

        osViewModel.fetchOpenSourceList();
    }


    private void observeAllEvents() {
        osViewModel.getOpenSourceList().observe(this,
                new Observer<List<OpenSource>>() {
                    @Override
                    public void onChanged(@Nullable List<OpenSource> openSourceList) {
                        updateOSAdapter(openSourceList);
                    }
                });

        osViewModel.getOpenOSDetailActivityEvent().observe(this,
                new Observer<OpenSource>() {
                    @Override
                    public void onChanged(@Nullable OpenSource openSource) {
                        openOSDetailActivity(openSource);
                    }
                });

        osViewModel.getPullToRefreshEvent().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isVisible) {
                        if (callback != null)
                            callback.updateSwipeRefreshLayoutTwo(isVisible);
                    }
                });

        osViewModel.getOpenSourceListReFetched().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        onOSListReFetched();
                    }
                });
    }


    private void updateOSAdapter(List<OpenSource> openSourceList) {
        osAdapter.updateListItems(openSourceList);
    }


    //This method call comes in from MainActivity
    public void setListScrollTop() {
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    //This method call comes in from MainActivity
    public void onParentCallToFetchList() {
        osViewModel.fetchOpenSourceList();
    }


    public void onOSListReFetched() {
        if (callback != null)
            callback.onOpenSourceListReFetched();
    }


    public void openOSDetailActivity(OpenSource openSource) {
        Intent intent = OSDetailsActivity.getStartIntent(getContext());
        intent.putExtra(OSDetailsActivity.KEY_PARCELABLE_OPEN_SOURCE, openSource);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Interactor.OpenSource) context;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
