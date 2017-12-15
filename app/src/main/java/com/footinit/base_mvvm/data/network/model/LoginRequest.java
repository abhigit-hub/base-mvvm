package com.footinit.base_mvvm.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhijit on 12-11-2017.
 */

public class LoginRequest {

    private LoginRequest() {
        //PC
    }

    public static class ServerLoginRequest {

        @Expose @SerializedName("email")
        private String email;

        @Expose @SerializedName("password")
        private String password;

        public ServerLoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class GoogleLoginRequest {

        @Expose @SerializedName("google_user_id")
        private String googleUserId;

        @Expose @SerializedName("google_id_token")
        private String googleToken;

        public GoogleLoginRequest(String googleUserId, String googleToken) {
            this.googleUserId = googleUserId;
            this.googleToken = googleToken;
        }

        public String getGoogleToken() {
            return googleToken;
        }

        public void setGoogleToken(String googleToken) {
            this.googleToken = googleToken;
        }
    }

    public static class FacebookLoginRequest {

        @Expose @SerializedName("fb_user_id")
        private String fbUserId;

        @Expose @SerializedName("fb_access_token")
        private String fbToken;

        public FacebookLoginRequest(String fbUserId, String fbToken) {
            this.fbUserId = fbUserId;
            this.fbToken = fbToken;
        }

        public String getFbUserId() {
            return fbUserId;
        }

        public void setFbUserId(String fbUserId) {
            this.fbUserId = fbUserId;
        }

        public String getFbToken() {
            return fbToken;
        }

        public void setFbToken(String fbToken) {
            this.fbToken = fbToken;
        }
    }
}
