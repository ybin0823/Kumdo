package com.nhnnext.android.kumdo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.model.Writing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 1. Server에서 단어를 Random하게 불러와야 한다(TODO 서버에서 가져올지, 로컬이 보유할지 설계 고민해볼 것)
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WriteActivity";
    public static final int LOAD_FROM_GALLERY = 1;
    public static final String SERVER_ADDRESS_SAVE = "http://10.64.192.61:3000/save";

    private LinearLayout mContainer;
    private Button mConcreteButton;
    private Button mAbstractButton;
    private Button mNatureButton;
    private ImageView mImageView;

    private User user;
    private Writing writing;
    private Set<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        initActionBar();

        initView();

        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");
        words = new HashSet<String>();
    }

    private void initView() {
        mConcreteButton = (Button) findViewById(R.id.concrete_button);
        mAbstractButton = (Button) findViewById(R.id.abstract_button);
        mNatureButton = (Button) findViewById(R.id.nature_button);

        mConcreteButton.setOnClickListener(this);
        mAbstractButton.setOnClickListener(this);
        mNatureButton.setOnClickListener(this);

        mContainer = (LinearLayout)findViewById(R.id.content_container);
        mImageView = (ImageView) findViewById(R.id.image_view);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Write");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Word word = new Word();
        mConcreteButton.setText(word.concreteWorld[new Random().nextInt(5)]);
        mAbstractButton.setText(word.abstractWorld[new Random().nextInt(5)]);
        mNatureButton.setText(word.natureWorld[new Random().nextInt(5)]);
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
        //TODO editText 클릭시 soft keyboard 출력
        mContainer.addView(mEditText);
    }

    /**
     * parameter : click된 버튼 id
     * editText 옆에 현재 클릭된 단어를 삽입한다
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
        Log.d("word", word);
        TextView mTextView= new TextView(this);
        mTextView.setText(word);
        mContainer.addView(mTextView);
        words.add(word);
    }

    public void onClickSave(View v) {
        final StringBuilder sentence = new StringBuilder();
        TextView textView;
        int count = mContainer.getChildCount();

        for (int i = 0; i < count; i++) {
            textView = (TextView) mContainer.getChildAt(i);
            if (textView.getText().length() != 0) {
                sentence.append(" " + textView.getText());
            }
        }

        if(sentence.length() == 0) {
            Toast.makeText(this, "글을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        writing = new Writing(user,  sentence.toString(), words.toArray(new String[words.size()]));
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(SERVER_ADDRESS_SAVE);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream ();

                    Gson gson = new Gson();
                    String json = gson.toJson(writing);
                    os.write(json.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // print result
                        Log.d(TAG, response.toString());
                    } else {
                        Log.d(TAG, "POST request not worked");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException : " + e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();

        Toast.makeText(this, sentence, Toast.LENGTH_SHORT).show();
    }

    public void loadImagefromGallery(View view) {
        // Google의 기본 Galley Application 실행
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // if you call startActivityForResult() using an intent that no app can handle,
        // your app will crash. So as long as the result is not null, it's safe to use the intent.
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, LOAD_FROM_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
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