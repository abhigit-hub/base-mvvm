package com.footinit.base_mvvm.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.footinit.base_mvvm.BuildConfig;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.data.db.model.User;
import com.footinit.base_mvvm.ui.base.BaseActivity;
import com.footinit.base_mvvm.ui.custom.CustomSwipeToRefresh;
import com.footinit.base_mvvm.ui.custom.RoundedImageView;
import com.footinit.base_mvvm.ui.login.LoginActivity;
import com.footinit.base_mvvm.ui.main.blog.BlogFragment;
import com.footinit.base_mvvm.ui.main.feed.FeedActivity;
import com.footinit.base_mvvm.ui.main.opensource.OSFragment;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class MainActivity extends BaseActivity<MainViewModel>
        implements Interactor.Blog, Interactor.OpenSource, HasSupportFragmentInjector {

    private boolean isCallbackSet = false;

    private Menu menu;

    private TextView tvUserName, tvUserEmail;

    private RoundedImageView ivProfilePic;

    ActionBarDrawerToggle drawerToggle;


    @Inject
    MainViewModel mainViewModel;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    MainPagerAdapter pagerAdapter;


    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.swipe_to_refresh)
    CustomSwipeToRefresh refreshLayout;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setUnBinder(ButterKnife.bind(this));

        setUp();

        observeAllEvents();
    }


    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }


    //INITIAL SET-UP
    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setUpNavMenu();
        mainViewModel.onNavMenuCreated();
        setUpViewPager();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainViewModel.onRefreshNetworkCall();
                startRefreshIconAnimation();
            }
        });
    }


    //DRAWER
    public void closeNavigationDrawer() {
        drawerLayout.closeDrawer(Gravity.START);
    }

    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    //SET-UP : CALLBACK FROM FRAGMENTS
    private void setUpCallbacks() {
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            WeakReference<Fragment> fragment = pagerAdapter.getRegisteredFragments().get(i);

            if (fragment != null) {
                if (fragment.get() instanceof BlogFragment) {
                    ((BlogFragment) fragment.get()).setParentCallBack(this);
                } else if (fragment.get() instanceof OSFragment) {
                    ((OSFragment) fragment.get()).setParentCallBack(this);
                }
            }

            fragment = null;
        }
    }

    private boolean isCallbackSet() {
        return isCallbackSet;
    }


    //CALLBACKS FROM FRAGMENTS
    @Override
    public void onBlogListReFetched() {
        WeakReference<Fragment> fragmentWeakReference =
                pagerAdapter.getRegisteredFragments().get(1);

        if (fragmentWeakReference != null)
            ((OSFragment) fragmentWeakReference.get()).onParentCallToFetchList();

        fragmentWeakReference = null;
    }

    @Override
    public void onBlogCallBackAdded() {
        isCallbackSet = true;
    }

    @Override
    public void onBlogCallBackRemoved() {
        isCallbackSet = false;
    }

    @Override
    public void onOpenSourceListReFetched() {
        WeakReference<Fragment> fragmentWeakReference =
                pagerAdapter.getRegisteredFragments().get(0);

        if (fragmentWeakReference != null)
            ((BlogFragment) fragmentWeakReference.get()).onParentCallToFetchList();

        fragmentWeakReference = null;
    }

    @Override
    public void onOpenSourceCallBackAdded() {
        isCallbackSet = true;
    }

    @Override
    public void onOpenSourceCallBackRemoved() {
        isCallbackSet = false;
    }


    //NAVIGATION MENU
    private void setUpNavMenu() {
        View headerLayout = navigationView.getHeaderView(0);

        tvUserName = headerLayout.findViewById(R.id.tv_name);
        tvUserEmail = headerLayout.findViewById(R.id.tv_email);
        ivProfilePic = (RoundedImageView) headerLayout.findViewById(R.id.iv_profile_picture);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
                    case R.id.nav_item_feed:
                        mainViewModel.onDrawerOptionFeedClicked();
                        return true;
                    case R.id.nav_item_logout:
                        mainViewModel.onDrawerOptionLogoutClicked();
                        return true;
                    default:
                        return false;
                }
            }
        });

        updateAppVersion();
    }


    //VIEWPAGER
    private void setUpViewPager() {
        pagerAdapter.setCount(2);

        viewPager.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.blog));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.open_source));

        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (pagerAdapter.getRegisteredFragments() != null
                        && pagerAdapter.getRegisteredFragments().size() > 0) {
                    if (!isCallbackSet())
                        setUpCallbacks();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //OBSERVE EVENTS FROM VIEW-MODEL
    private void observeAllEvents() {
        //EVENT (transmit DATA)
        mainViewModel.getPullToRefreshEvent().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean status) {
                        updateSwipeRefreshLayout(status);
                    }
                });

        mainViewModel.getBlogList().observe(this,
                new Observer<List<Blog>>() {
                    @Override
                    public void onChanged(@Nullable List<Blog> blogList) {
                        updateBlogList(blogList);
                    }
                });

        mainViewModel.getOpenSourceList().observe(this,
                new Observer<List<OpenSource>>() {
                    @Override
                    public void onChanged(@Nullable List<OpenSource> openSourceList) {
                        updateOpenSourceList(openSourceList);
                    }
                });

        mainViewModel.getUser().observe(this,
                new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        updateUserViews(user);
                    }
                });


        //EVENT
        mainViewModel.getResetAllAdapterEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        resetAllAdapterPositions();
                    }
                });

        mainViewModel.getOpenLoginActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openLoginActivity();
                    }
                });

        mainViewModel.getOpenFeedActivityEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        openFeedActivity();
                    }
                });

        mainViewModel.getCloseNavigationDrawerEvent().observe(this,
                new Observer<Void>() {
                    @Override
                    public void onChanged(@Nullable Void aVoid) {
                        closeNavigationDrawer();
                    }
                });
    }


    //ANIMATION
    private void startRefreshIconAnimation() {
        if (menu != null) {
            Drawable drawable = menu.findItem(R.id.action_refresh).getIcon();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }


    //UPDATE VIEWS
    public void resetAllAdapterPositions() {
        viewPager.setCurrentItem(0);
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            WeakReference<Fragment> fragment = pagerAdapter.getRegisteredFragments().get(i);

            if (fragment != null) {
                if (fragment.get() instanceof BlogFragment) {
                    ((BlogFragment) fragment.get()).setListScrollTop();
                } else if (fragment.get() instanceof OSFragment) {
                    ((OSFragment) fragment.get()).setListScrollTop();
                }
            }

            fragment = null;
        }
    }

    public void updateSwipeRefreshLayout(boolean isVisible) {
        refreshLayout.setRefreshing(isVisible);
    }

    public void updateBlogList(List<Blog> blogList) {
        if (blogList != null) {
            WeakReference<Fragment> fragment = pagerAdapter.getRegisteredFragments().get(0);

            if (fragment != null && fragment.get() instanceof BlogFragment) {
                ((BlogFragment) fragment.get()).updateBlogList(blogList);
            }

            fragment = null;
        }
    }

    public void updateOpenSourceList(List<OpenSource> openSourceList) {
        if (openSourceList != null) {
            WeakReference<Fragment> fragment = pagerAdapter.getRegisteredFragments().get(1);

            if (fragment != null && fragment.get() instanceof OSFragment) {
                ((OSFragment) fragment.get()).updateOSList(openSourceList);
            }

            fragment = null;
        }
    }

    public void updateUserViews(User user) {
        if (user != null) {
            if (user.getUserName() != null)
                tvUserName.setText(user.getUserName());
            if (user.getEmail() != null)
                tvUserEmail.setText(user.getEmail());
        }
    }

    public void updateAppVersion() {
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        tvAppVersion.setText(version);
    }

    //NAVIGATION
    public void openLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    public void openFeedActivity() {
        startActivity(FeedActivity.getStartIntent(this));
    }


    //MENU ITEMS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        switch (item.getItemId()) {
            case R.id.action_refresh:
                mainViewModel.onRefreshNetworkCall();
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        unlockDrawer();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
