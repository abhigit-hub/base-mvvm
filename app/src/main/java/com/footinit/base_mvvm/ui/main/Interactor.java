package com.footinit.base_mvvm.ui.main;

/**
 * Created by Abhijit on 23-11-2017.
 */

public interface Interactor {

    interface Blog {
        void onBlogListReFetched();

        void updateSwipeRefreshLayoutOne(boolean isVisible);
    }


    interface OpenSource {
        void onOpenSourceListReFetched();

        void updateSwipeRefreshLayoutTwo(boolean isVisible);
    }
}
