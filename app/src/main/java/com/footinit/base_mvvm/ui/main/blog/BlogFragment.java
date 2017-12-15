package com.footinit.base_mvvm.ui.main.blog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.ui.base.BaseFragment;
import com.footinit.base_mvvm.ui.main.Interactor;
import com.footinit.base_mvvm.ui.main.blogdetails.BlogDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class BlogFragment extends BaseFragment<BlogViewModel> {

    private Interactor.Blog callback;

    private BlogViewModel blogViewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    BlogAdapter blogAdapter;

    @Inject
    LinearLayoutManager linearLayoutManager;


    @BindView(R.id.rv_blog)
    RecyclerView rvBlog;


    public static BlogFragment newInstance() {
        Bundle args = new Bundle();
        BlogFragment fragment = new BlogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setParentCallBack(Interactor.Blog callback) {
        this.callback = callback;
        this.callback.onBlogCallBackAdded();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        blogAdapter.setCallback(blogViewModel);

        observeAllEvents();

        return view;
    }

    @Override
    public BlogViewModel getViewModel() {
        blogViewModel = ViewModelProviders.of(this, factory).get(BlogViewModel.class);
        return blogViewModel;
    }

    @Override
    protected void setUp(View view) {
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvBlog.setLayoutManager(linearLayoutManager);
        rvBlog.setItemAnimator(new DefaultItemAnimator());
        rvBlog.setAdapter(blogAdapter);

        blogViewModel.fetchBlogList();
    }


    private void observeAllEvents() {
        blogViewModel.getBlogList().observe(this,
                new Observer<List<Blog>>() {
                    @Override
                    public void onChanged(@Nullable List<Blog> blogs) {
                        updateBlogAdapter(blogs);
                    }
                });

        blogViewModel.getOpenBlogDetailActivity().observe(this,
                new Observer<Blog>() {
                    @Override
                    public void onChanged(@Nullable Blog blog) {
                        openBlogDetailActivity(blog);
                    }
                });

        blogViewModel.getBlogListReFetched().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        onBlogListReFetched();
                    }
                });
    }


    private void updateBlogAdapter(List<Blog> blogList) {
        blogAdapter.updateListItems(blogList);
    }


    //This method call comes in from MainActivity
    public void updateBlogList(List<Blog> blogList) {
        blogViewModel.onUpdateBlogList(blogList);
    }

    //This method call comes in from MainActivity
    public void setListScrollTop() {
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    //This method call comes in from MainActivity
    public void onParentCallToFetchList() {
        blogViewModel.fetchBlogList();
    }




    public void onBlogListReFetched() {
        if (callback != null)
            callback.onBlogListReFetched();
    }


    public void openBlogDetailActivity(Blog blog) {
        Intent intent = BlogDetailsActivity.getStartIntent(getContext());
        intent.putExtra(BlogDetailsActivity.KEY_PARCELABLE_BLOG, blog);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        blogAdapter.removeCallback();
        if (callback != null) {
            callback.onBlogCallBackRemoved();
            callback = null;
        }
        super.onDestroyView();
    }
}
