package com.footinit.base_mvvm.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.footinit.base_mvvm.ui.main.blog.BlogFragment;
import com.footinit.base_mvvm.ui.main.opensource.OSFragment;

import java.lang.ref.WeakReference;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private SparseArray<WeakReference<Fragment>> registeredFragments =
            new SparseArray<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        tabCount = 0;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BlogFragment.newInstance();
            case 1:
                return OSFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, new WeakReference<Fragment>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setCount(int tabCount) {
        this.tabCount = tabCount;
    }

    public SparseArray<WeakReference<Fragment>> getRegisteredFragments() {
        return registeredFragments;
    }
}
