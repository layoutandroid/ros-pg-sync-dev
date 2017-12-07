package com.jaison.bompod.manager;

import android.util.Log;

import com.jaison.bompod.constant.RealmConfig;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.authProvider.UsernameAuthProvider;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by jaison on 18/10/17.
 */

public class UserManager {

    private static final String TAG = "UserManager";
    public static boolean isLoggedIn() {
        Log.i(TAG, "Realm Login -> " + (SyncUser.currentUser() != null));
        Log.i(TAG, "Hasura Login -> "+ Hasura.getClient().getUser().isLoggedIn());
        return SyncUser.currentUser() != null && Hasura.getClient().getUser().isLoggedIn();
    }
    public static void login(final String username, final String password, final LoginListener listener) {
        Hasura.getClient().getUser().login(new UsernameAuthProvider(username, password), new AuthResponseListener() {
            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "Hasura Login Successful");
                //Log in
                SyncCredentials credentials = SyncCredentials.usernamePassword("calica", "android007", false);
                SyncUser.loginAsync(credentials, RealmConfig.URL.AUTH, new SyncUser.Callback<SyncUser>() {
                    @Override
                    public void onSuccess(SyncUser result) {
                        Log.i(TAG, "Realm Login Successful");
                        if(listener!=null) {
                            listener.onLoginSuccessful();
                        }
                    }
                    @Override
                    public void onError(ObjectServerError error) {
                        Log.i(TAG, "Realm login Failed");
                        if (listener != null)
                            listener.onLoginFailed(error);
                    }
                });
            }
            @Override
            public void onFailure(HasuraException e) {
                Log.i(TAG, "Hasura Login failed");
                if (listener != null) {
                    listener.onLoginFailed(e);
                }
            }
        });
    }
    public interface LoginListener {
        void onLoginSuccessful();

        void onLoginFailed(Object error);
    }

}
