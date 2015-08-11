package com.nhnnext.android.kumdo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

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

        // SplashActivity 확인을 위해 2초 동안 지연하는 동작
        // TODO 추후에 네트워크 기능 추가 되면 제거
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (OAuthLoginState.OK.equals(OAuthLogin.getInstance().getState(getApplicationContext()))) {
                    // access token 이 있는 상태로 바로 메뉴로 전환
                    showMenu();
                } else {
                    showNaverLogin();
                }
            }
        }, 2000);
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
