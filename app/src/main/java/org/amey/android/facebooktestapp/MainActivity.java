package org.amey.android.facebooktestapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ShareButton shareButton;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // first initlalize facebook sdk befor performing any action.
        initializeFacebookSdk();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        shareButton = (ShareButton) findViewById(R.id.share_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "App Id:  " + loginResult.getAccessToken().getApplicationId());
                Log.i(TAG, "Access Token: " + loginResult.getAccessToken().getToken());
                Log.i(TAG, "User Id: " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "Successful login");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Login cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.w(TAG, "Login Error");
            }
        });
        final ShareLinkContent shareContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareButton.setShareContent(shareContent);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Share Button Clicked");

                shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);

            }
        });

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i(TAG, "Share ID: "+result.getPostId());
                Log.i(TAG, "success");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.w(TAG, "share Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
