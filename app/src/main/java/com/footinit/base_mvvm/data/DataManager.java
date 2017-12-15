package com.footinit.base_mvvm.data;

import com.footinit.base_mvvm.data.db.DbHelper;
import com.footinit.base_mvvm.data.network.ApiHelper;
import com.footinit.base_mvvm.data.prefs.PreferenceHelper;

/**
 * Created by Abhijit on 08-11-2017.
 */

public interface DataManager extends DbHelper, PreferenceHelper, ApiHelper {

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_LOGGED_GOOGLE(1),
        LOGGED_IN_MODE_LOGGED_FB(2),
        LOGGED_IN_MODE_LOGGED_SERVER(3);

        private final int type;

        LoggedInMode(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
