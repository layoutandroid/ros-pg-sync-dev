package com.jaison.bompod.ui;

import android.os.Bundle;
import android.util.Log;

import com.jaison.bompod.R;
import com.jaison.bompod.manager.UserManager;

public class LauncherActivity extends BaseActivity {

    private static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "UserManager.isLoggedIn -> " + UserManager.isLoggedIn());
        if (UserManager.isLoggedIn()) {
            MainActivity.launchActivity(this);
        } else {
            AuthenticationActivity.launchActivity(this);
        }
    }
}
