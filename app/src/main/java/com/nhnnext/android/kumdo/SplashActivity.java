package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

/**
 * 처음 Application 시작 시
 * 1. 소셜 로그인 상태 확인
 * 2. 네트워크 상태 확인
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (OAuthLoginState.OK.equals(OAuthLogin.getInstance().getState(getApplicationContext()))) {
                // access token 이 있는 상태로 바로 메뉴로 전환
                showMenu();
            } else {
                showNaverLogin();
            }
        } else {
            //TODO change to dialog
            Toast.makeText(this, "No network connection available!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void showNaverLogin() {
        Intent intent = new Intent(this, OAuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
