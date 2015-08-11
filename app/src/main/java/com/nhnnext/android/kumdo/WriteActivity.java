package com.nhnnext.android.kumdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * 1. Server에서 단어를 Random하게 불러와야 한다
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WriteActivity";

    LinearLayout container;
    Button concreteButton;
    Button abstractButton;
    Button natureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Write");

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
    public void onClickEditText(View v) {
        EditText mEditText = new EditText(this);
        mEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.requestFocus();
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

    public void onClickSave(View v) {
        int count = container.getChildCount();

        String sentence = "";
        TextView tv;
        for (int i = 0; i < count; i++) {
            tv = (TextView) container.getChildAt(i);

            //TODO String class에서 += 를 쓰는 것이 좋은 방법인지 찾아볼 것
            sentence += tv.getText().toString();
        }

        if(sentence.isEmpty()) {
            Toast.makeText(this, "글을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, sentence, Toast.LENGTH_SHORT).show();
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);
    }

    // 추후에는 Server 내에 word table을 유지할 예정
    private class Word {
        private String [] abstractWorld = {
                "love", "pleasure", "happy", "sadness", "angry"
        };
        private String [] concreteWorld = {
                "book", "hand", "chicken", "bus", "pencil"
        };
        private String [] natureWorld = {
                "sun", "water", "sea", "river", "mouatain"
        };
    }
}