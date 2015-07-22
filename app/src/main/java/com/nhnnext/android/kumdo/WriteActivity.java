package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 1. Server에서 단어를 Random하게 불러와야 한다
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 */
public class WriteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //서버로부터 단어를 불러와서 View로 그려줌 : loadWord()
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 저장된 데이터가 있으면 View로 보여주고 없으면
        // 서버에서 load 된 단어를 View로 보여준다
    }

    @Override
    protected void onPause() {
        super.onPause();

        //현재까지 저장된 단어를 임시저장한다
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
     * method : loadWord()
     * 서버로부터 단어를 읽어온다
     */


    /**
     * method : onClickEditText()
     * edit 버튼을 클릭하면 화면에 editText창을 생성
     */

    /**
     * method : onClickWord()
     * parameter : click된 버튼 id
     * editText 안(or 옆)에 현재 클릭된 단어를 삽입한다
     */
}