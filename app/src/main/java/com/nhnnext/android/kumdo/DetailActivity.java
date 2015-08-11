package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * 1. 현재 클릭 된 아이템의 ID(Intent로 구현할지 다른 방법을 사용할지는 추후 논의)를 받는다
 * 2. 서버로 현재 아이템의 ID를 보내서 저장 된 데이터를 불러온다
 * 3. Image와 Text로 View를 그려준다
 * 4. 댓글을 달 수 있다.
 * 5. 좋아요를 누를 수 있다.
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail");

        //상세보기 클릭 한 아이템 정보를 서버에서 가져온다 : loadDetail()
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Image, Text 등의 View를 그려준다(onResume, onStart, onCreate 중에서 고민)
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

    /**
     * method : loadDetail()
     * parameter : Item Id
     * id로 서버에 저장된 데이터를 불러와서 View를 그려준다
     */

    /**
     * method : showComment()
     * 댓글 창을 보여준다
     */

    /**
     * method : createComment()
     * 입력한 댓글을 서버로 전송한다
     */

    /**
     * method : onClickLike()
     * parameter : Item Id
     * 좋아요 버튼을 누르면 Id의 Like count가 증가한다
     */
}
