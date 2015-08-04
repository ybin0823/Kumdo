package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * 1. Server에서 단어를 Random하게 불러와야 한다
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WriteActivity.class.getName();

    LinearLayout container;
    Button concreteButton;
    Button abstractButton;
    Button natureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        concreteButton = (Button) findViewById(R.id.concrete_button);
        abstractButton = (Button) findViewById(R.id.abstract_button);
        natureButton = (Button) findViewById(R.id.nature_button);

        concreteButton.setOnClickListener(this);
        abstractButton.setOnClickListener(this);
        natureButton.setOnClickListener(this);

        container = (LinearLayout)findViewById(R.id.content_container);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Word word = new Word();
        concreteButton.setText(word.concreteWorld[new Random().nextInt(5)]);
        abstractButton.setText(word.abstractWorld[new Random().nextInt(5)]);
        natureButton.setText(word.natureWorld[new Random().nextInt(5)]);
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
    public void onClickEditText(View view) {
        EditText mEditText = new EditText(this);
        container.addView(mEditText);
    }

    /**
     * method : onClickWord()
     * parameter : click된 버튼 id
     * editText 안(or 옆)에 현재 클릭된 단어를 삽입한다
     */
    @Override
    public void onClick(View v) {
        // 다음 3가지 경우에만 addWord method 실행
        switch(v.getId()) {
            case R.id.abstract_button:
            case R.id.concrete_button:
            case R.id.nature_button:
                addWord(v);
                break;
        }
    }

    public void addWord(View v) {
        Button button = (Button) v;
        String word = button.getText().toString();
        TextView mTextView= new TextView(this);
        mTextView.setText(word);
        container.addView(mTextView);
    }

    // 추후에는 Server 내에 word table을 유지할 예정
    private class Word {
        private String [] abstractWorld = {
                "우정", "기쁨", "사랑", "따뜻", "냉정"
        };
        private String [] concreteWorld = {
                "책", "병", "피자", "치킨", "운동"
        };
        private String [] natureWorld = {
                "해", "물", "바다", "강", "산"
        };
    }
}