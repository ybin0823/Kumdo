package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        mOAuthLoginInstance = OAuthLogin.getInstance();
    }

    public void logout(View v) {
        if(OAuthLoginState.OK.equals(mOAuthLoginInstance.getState(mContext))) {
            mOAuthLoginInstance.logout(mContext);
            if(!OAuthLoginState.OK.equals(mOAuthLoginInstance.getState(mContext))) {
                Intent intent = new Intent(this, OAuthActivity.class);
                startActivity(intent);
            }
        }
    }
}