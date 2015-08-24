package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

public class SettingActivity extends AppCompatActivity {
    private OAuthLogin mOAuthLoginInstance;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = this;

        initActionBar();

        mOAuthLoginInstance = OAuthLogin.getInstance();
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.settings_title);
    }

    public void logout(View v) {
        if(OAuthLoginState.OK.equals(mOAuthLoginInstance.getState(mContext))) {
            mOAuthLoginInstance.logout(mContext);
            if(!OAuthLoginState.OK.equals(mOAuthLoginInstance.getState(mContext))) {
                Intent intent = new Intent(this, SnsLoginActivity.class);
                startActivity(intent);
            }
        }
    }
}