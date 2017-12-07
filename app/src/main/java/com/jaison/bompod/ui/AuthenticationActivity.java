package com.jaison.bompod.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jaison.bompod.R;
import com.jaison.bompod.manager.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jaison on 18/10/17.
 */

public class AuthenticationActivity extends BaseActivity {

    private static final String TAG = "AuthenticationActivity";
    Activity ac;

    public static void launchActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, AuthenticationActivity.class));
    }

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginButton)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ac=AuthenticationActivity.this;
        ButterKnife.bind(this);

        username.setText("calica");
        password.setText("android007");
    }

    @OnClick(R.id.loginButton)
    public void onViewClicked() {
        showProgressDialog("Logging in");
        UserManager.login(username.getText().toString(), password.getText().toString(), new UserManager.LoginListener() {
            @Override
            public void onLoginSuccessful() {
                Log.i(TAG, "Login Successful");
                dismissProgressDialog();
               // MainActivity.launchActivity(ac);
                AuthenticationActivity.this.finish();
            }

            @Override
            public void onLoginFailed(Object error) {
                dismissProgressDialog();
                Log.e(TAG, "Login failed: " + error.toString());
                showToast(error.toString());
            }
        });
    }
}
