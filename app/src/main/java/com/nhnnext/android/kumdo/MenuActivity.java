package com.nhnnext.android.kumdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nhnnext.android.kumdo.fragment.BestFragment;
import com.nhnnext.android.kumdo.fragment.CategoryFragment;
import com.nhnnext.android.kumdo.fragment.MylistFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            BestFragment bestFragment = new BestFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, bestFragment).commit();
        }
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

    //TODO Fragment 전환 시 Button 동작 안됨
    //TODO method 중복 심함. 다른 방법 고려

    public void replaceToHome(View view) {
        BestFragment bestFragment = new BestFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, bestFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceToCategory(View view) {
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, categoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceToMylist(View view) {
        MylistFragment mylistFragment = new MylistFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mylistFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeToWrite(View view) {
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }
}