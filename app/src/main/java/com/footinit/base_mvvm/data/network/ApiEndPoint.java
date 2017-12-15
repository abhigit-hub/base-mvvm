package com.footinit.base_mvvm.data.network;

import com.footinit.base_mvvm.utils.AppConstants;

/**
 * Created by Abhijit on 10-11-2017.
 */

public final class ApiEndPoint {

    static final String ENDPOINT_GOOGLE_LOGIN = AppConstants.BASE_URL +
            "588d14f4100000a9072d2943";

    static final String ENDPOINT_FACEBOOK_LOGIN = AppConstants.BASE_URL +
            "588d15d3100000ae072d2944";

    static final String ENDPOINT_SERVER_LOGIN = AppConstants.BASE_URL +
            "588d15f5100000a8072d2945";

    public static final String ENDPOINT_LOGOUT = AppConstants.BASE_URL +
            "588d161c100000a9072d2946";

    public static final String ENDPOINT_OPEN_SOURCE = AppConstants.BASE_URL +
            "5a0ed84c3000001c33433763";

    public static final String ENDPOINT_BLOG = AppConstants.BASE_URL +
            "5a0eda243000000a35433770";

    private ApiEndPoint() {
        //PC
    }
}
