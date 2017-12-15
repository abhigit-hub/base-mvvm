package com.footinit.base_mvvm.ui.main.opensourcedetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 10-12-2017.
 */

public class OSDetailsActivity extends BaseActivity<OSDetailsViewModel> {

    public static final String KEY_PARCELABLE_OPEN_SOURCE = "KEY_PARCELABLE_OPEN_SOURCE";
    private OpenSource currentOpenSource;
    private OSDetailsViewModel osDetailsViewModel;


    @Inject
    ViewModelProvider.Factory factory;


    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.ctl_os_detail)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.iv_cover_img)
    ImageView ivCover;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_author)
    TextView tvAuthor;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.fab_os)
    FloatingActionButton fab;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OSDetailsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_os_details);

        currentOpenSource = getIntent().getParcelableExtra(KEY_PARCELABLE_OPEN_SOURCE);

        setUnBinder(ButterKnife.bind(this));

        observeAllEvents();

        setUp();
    }

    @Override
    public OSDetailsViewModel getViewModel() {
        osDetailsViewModel = ViewModelProviders.of(this, factory).get(OSDetailsViewModel.class);
        return osDetailsViewModel;
    }

    private void observeAllEvents() {
        osDetailsViewModel.getReturnToMainActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        returnToMainActivity();
                    }
                });

        osDetailsViewModel.getOpenInBrowserEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openInBrowser();
                    }
                });
    }

    @Override
    protected void setUp() {
        setUpCollapsingToolbarLayout();
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (currentOpenSource != null) {
            if (currentOpenSource.getImgUrl() != null)
                Glide.with(this)
                        .load(currentOpenSource.getImgUrl())
                        .asBitmap()
                        .fitCenter()
                        .placeholder(R.drawable.placeholder)
                        .into(ivCover);

            if (currentOpenSource.getTitle() != null)
                tvTitle.setText(currentOpenSource.getTitle());

            if (currentOpenSource.getAuthor() != null)
                tvAuthor.setText(currentOpenSource.getAuthor());

            if (currentOpenSource.getDescription() != null)
                tvDescription.setText(currentOpenSource.getDescription());
        } else {
            showSnackbar(getString(R.string.there_was_an_error_display_open_source));
            osDetailsViewModel.onOSDetailsDisplayedError();
        }

        setUpAnimation();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentOpenSource != null)
                    osDetailsViewModel.onOpenSourceFABClicked();
            }
        });
    }

    private void setUpAnimation() {
        Drawable drawable = fab.getDrawable();

        if (drawable != null && drawable instanceof Animatable)
            ((Animatable) drawable).start();
        else if (drawable instanceof AnimatedVectorDrawableCompat)
            ((AnimatedVectorDrawableCompat) drawable).start();
        else if (drawable instanceof AnimatedVectorDrawable)
            ((AnimatedVectorDrawable) drawable).start();
    }

    private void setUpCollapsingToolbarLayout() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.open_source_details));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                    setUpAnimation();
                }
            }
        });
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

    public void returnToMainActivity() {
        onBackPressed();
    }

    public void openInBrowser() {
        if (currentOpenSource != null && currentOpenSource.getProjectUrl() != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(currentOpenSource.getProjectUrl()));
            startActivity(intent);
        }
    }
}

