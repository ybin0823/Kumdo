package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 3개의 Fragment로 구성된다
 * 1. 첫 번째 Fragment는 서버에 저장된 최신 Image와 Text를 보여준다
 * 2. 두 번째 Fragment는 카테고리 메뉴 View를 보여준다
 * 3. 세 번째 Fragment는 내가 작성한 데이터의 목록을 보여준다
 */

/*
 * FragmentActivity는 API Level 11 이하 버전에서 fragment를 사용하기 위해 Support Library를 지원하는
 * Activity이다. min sdk가 16이므로 Activity클래스로도 충분해서 수정 `15.07.30 by jyb
 */
public class MenuActivity extends AppCompatActivity {

    /**
     * TODO FrgmentManager를 사용하여 runtime시에 fragment를 생성
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
