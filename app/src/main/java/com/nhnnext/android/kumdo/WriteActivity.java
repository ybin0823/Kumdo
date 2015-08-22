package com.nhnnext.android.kumdo;

import android.content.Context;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.model.Writing;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 1. 단어를 Random하게 불러와야 한다
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WriteActivity";
    public static final int LOAD_FROM_GALLERY = 1;
    private static final int GET_CATEGORY = 2;
    public static final String UPLOAD_TO_SERVER = "http://192.168.0.3:3000/upload";
    public static final int No_CATEGORY = 0;

    private Context mContext;

    private LinearLayout mContainer;
    private Button mConcreteButton;
    private Button mAbstractButton;
    private Button mNatureButton;
    private ImageView mImageView;

    private User user;
    private Writing writing;
    private Set<String> usedWords;
    private String mImagePath;
    private int mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mContext = this;

        initActionBar();

        initView();

        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");

        usedWords = new HashSet<String>();
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

        TextView mTextView= new TextView(mContext);
        mTextView.setText(word);
        mContainer.addView(mTextView);
        usedWords.add(word);
    }

    //TODO validation check about text, word
    public void onClickSave(View v) {
        if (mImagePath == null)  {
            Toast.makeText(this, "Please select your image", Toast.LENGTH_SHORT).show();
            return;
        }
        showCategoryDialog();
    }

    private String getSetence() {
        StringBuilder sentence = new StringBuilder();
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
            return null;
        }
        return sentence.toString();
    }

    private void sendMultipart(String sentence, String words, String date) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("sentence", sentence);
        params.put("words", words);
        params.put("category", mCategory);
        params.put("date", date);

        try {
            params.put("image", new File(mImagePath));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException : " + e);
        }

        client.post(UPLOAD_TO_SERVER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(getApplicationContext(), "Save Success!!!", Toast.LENGTH_SHORT).show();
                finish();
                //TODO save data to SQLite
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private boolean setWriting(String sentence, String words, String date) {
        writing = new Writing(user.getName(), user.getEmail(), sentence,
                words, "", mCategory, date);
        return true;
    }

    public void pickImagefromGallery(View view) {
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
        switch(requestCode) {
            case LOAD_FROM_GALLERY:
                if(resultCode == RESULT_OK) {
                    loadImageFromGallery(data);
                }
                break;
            case GET_CATEGORY:
                if(resultCode == RESULT_OK) {
                    mCategory = data.getIntExtra("category", No_CATEGORY);
                    if(mCategory != No_CATEGORY) {
                        String sentence = getSetence();
                        String words = Arrays.toString(usedWords.toArray(new String[usedWords.size()]));
                        String date = String.valueOf(System.currentTimeMillis());

                        // set wrting
                        setWriting(sentence, words, date);

                        // send data to server
                        sendMultipart(sentence, words, date);

                    } else {
                        Toast.makeText(this, "Please choose category", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void loadImageFromGallery(Intent data) {
        Uri selectedImage = data.getData();

        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        mImagePath = picturePath;

        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    public void showCategoryDialog() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivityForResult(intent, GET_CATEGORY);
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