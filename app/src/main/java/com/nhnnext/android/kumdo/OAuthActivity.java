package com.nhnnext.android.kumdo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

/**
 * 네이버 아이디로 로그인 기능을 위한 Activity
 */
public class OAuthActivity extends AppCompatActivity {
    private static final String TAG = "OAuthActivity";

    /**
     * client 정보
     */
    private static String OAUTH_CLIENT_ID = "koJsYBs4oGVTaGDYO_Ke";
    private static String OAUTH_CLIENT_SECRET = "RSWJ9fqF1K";
    private static String OAUTH_CLIENT_NAME = "백일장";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    private OAuthLoginButton mOAuthLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_login);

        // logcat 로그에 네이버 아이디로 로그인 로그를 확인할 수 있게 하기 위해 true
        // 배포 버전에서는 false로 할 것.
        OAuthLoginDefine.DEVELOPER_VERSION = true;

        mContext = this;

        /*
		 * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
		 * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
		 */
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
       // TODO Button OAuthLoginHandler 등록
    }
}