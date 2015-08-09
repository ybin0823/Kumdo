package com.nhnnext.android.kumdo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // fragment를 위한 FrameLayout 이 있는지 체크
        if (findViewById(R.id.fragment_container) != null) {

            // 이전에 저장된 상태가 있다면 굳이 아래를 실행할 필요가 없기에 return
            if (savedInstanceState != null) {
                return;
            }

            // App 시작 시 첫 Fragment Instance 생성
            BestFragment bestFragment = new BestFragment();
            getFragmentManager().beginTransaction()
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

    //TODO method 중복 심함. 다른 방법 고려

    public void replaceToHome(View view) {
        BestFragment bestFragment = new BestFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, bestFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceToCategory(View view) {
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, categoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceToMylist(View view) {
        MylistFragment mylistFragment = new MylistFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mylistFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeToWrite(View view) {
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_write:
                openWrite();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWrite() {
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }

    private void openSettings() {
        //TODO setting Activity로 전환
    }
}
