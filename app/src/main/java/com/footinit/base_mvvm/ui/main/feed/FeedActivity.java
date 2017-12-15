package com.footinit.base_mvvm.ui.main.feed;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseActivity;
import com.footinit.base_mvvm.ui.main.blogdetails.BlogDetailsActivity;
import com.footinit.base_mvvm.ui.main.opensourcedetails.OSDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 10-12-2017.
 */

public class FeedActivity extends BaseActivity<FeedViewModel> {

    private FeedViewModel feedViewModel;


    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    LinearLayoutManager layoutManager;

    @Inject
    FeedAdapter adapter;


    @BindView(R.id.rv_feed)
    RecyclerView rvFeed;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed);

        setUnBinder(ButterKnife.bind(this));

        adapter.setCallback(feedViewModel);

        observeAllEvents();

        setUp();
    }

    @Override
    public FeedViewModel getViewModel() {
        feedViewModel = ViewModelProviders.of(this, factory).get(FeedViewModel.class);
        return feedViewModel;
    }

    private void observeAllEvents() {
        feedViewModel.getOpenBlogDetailsActivityEvent().observe(this,
                new Observer<Blog>() {
                    @Override
                    public void onChanged(@Nullable Blog blog) {
                        openBlogDetailsActivity(blog);
                    }
                });

        feedViewModel.getOpenOSDetailsActivityEvent().observe(this,
                new Observer<OpenSource>() {
                    @Override
                    public void onChanged(@Nullable OpenSource openSource) {
                        openOSDetailsActivity(openSource);
                    }
                });

        feedViewModel.getList().observe(this,
                new Observer<List<Object>>() {
                    @Override
                    public void onChanged(@Nullable List<Object> objectList) {
                        onListRetrieved(objectList);
                    }
                });
    }

    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvFeed.setLayoutManager(layoutManager);
        rvFeed.setItemAnimator(new DefaultItemAnimator());
        rvFeed.setAdapter(adapter);

        feedViewModel.onViewPrepared();
    }

    public void onListRetrieved(List<Object> list) {
        adapter.addItems(list);
    }

    public void openBlogDetailsActivity(Blog blog) {
        Intent intent = BlogDetailsActivity.getStartIntent(this);
        intent.putExtra(BlogDetailsActivity.KEY_PARCELABLE_BLOG, blog);
        startActivity(intent);
    }

    public void openOSDetailsActivity(OpenSource openSource) {
        Intent intent = OSDetailsActivity.getStartIntent(this);
        intent.putExtra(OSDetailsActivity.KEY_PARCELABLE_OPEN_SOURCE, openSource);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        adapter.removeCallback();
        super.onDestroy();
    }
}
