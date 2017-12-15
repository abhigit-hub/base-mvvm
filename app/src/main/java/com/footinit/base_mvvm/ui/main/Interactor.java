package com.footinit.base_mvvm.ui.main;

/**
 * Created by Abhijit on 23-11-2017.
 */

public interface Interactor {

    interface Blog {
        void onBlogListReFetched();

        void onBlogCallBackAdded();

        void onBlogCallBackRemoved();
    }


    interface OpenSource {
        void onOpenSourceListReFetched();

        void onOpenSourceCallBackAdded();

        void onOpenSourceCallBackRemoved();
    }
}
