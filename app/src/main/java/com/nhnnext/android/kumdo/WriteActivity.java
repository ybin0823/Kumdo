package com.nhnnext.android.kumdo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhnnext.android.kumdo.db.WritingOpenHelper;
import com.nhnnext.android.kumdo.model.Category;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.util.FlowLayout;
import com.nhnnext.android.kumdo.util.RequestUrl;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 1. 단어를 Random하게 불러와야 한다
 * 2. EditText를 클릭하면 글을 입력할 수 있는 창이 생성 된다
 * 3. 단어를 클릭하면 EditText 뒤에 단어가 생성된다
 * 4. 저장하기를 누르면 내용이 서버로 전송된다
 * 글자는 200자로 제한한다.
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WriteActivity";
    public static final int LOAD_FROM_GALLERY = 1;
    private static final int GET_CATEGORY = 2;
    public static final String SPACE = " ";
    public static final int MAX_SENTENCE_LENGTH = 200;

    private Context mContext;

    private FlowLayout mContainer;
    private Button mConcreteButton;
    private Button mAbstractButton;
    private Button mNatureButton;
    private ImageView mImageView;

    private User user;
    private Writing writing;
    private Set<String> usedWords;
    private String mImagePath;
    private int mCategory;

    private boolean enableEdit;
    private TextWatcher textWatcher;

    WritingOpenHelper mDbHelper;
    SQLiteDatabase db;

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
        enableEdit = true;
        mDbHelper = new WritingOpenHelper(mContext);
    }

    private void initView() {
        mConcreteButton = (Button) findViewById(R.id.concrete_button);
        mAbstractButton = (Button) findViewById(R.id.abstract_button);
        mNatureButton = (Button) findViewById(R.id.nature_button);

        mConcreteButton.setOnClickListener(this);
        mAbstractButton.setOnClickListener(this);
        mNatureButton.setOnClickListener(this);

        mContainer = (FlowLayout)findViewById(R.id.content_container);
        mImageView = (ImageView) findViewById(R.id.image_view);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.write_title);
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
     * 연속 2번 생성은 안된다
     */
    public void onClickEditText(View v) {
        if (enableEdit && getSentence().length() < MAX_SENTENCE_LENGTH ) {
            EditText mEditText = new EditText(this);
            mEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mEditText.requestFocus();
            mContainer.addView(mEditText);
            textWatcher = new EditTextWatcher(getSentence().length());
            mEditText.addTextChangedListener(textWatcher);

            showSoftKeyboard(mEditText);
            // 연속 2번 생성 방지를 위한 flag
            enableEdit = false;
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * parameter : click된 버튼 id
     * editText 옆에 현재 클릭된 단어를 삽입한다
     */
    @Override
    public void onClick(View v) {
        if(getSentence().length() < MAX_SENTENCE_LENGTH) {
            // 다음 3가지 경우에만 addWord method 실행
            switch(v.getId()) {
                case R.id.abstract_button:
                case R.id.concrete_button:
                case R.id.nature_button:
                    addWord(v);
                    break;
            }
            return;
        }
        Toast.makeText(this, R.string.too_long_text, Toast.LENGTH_SHORT).show();
    }

    private String getSentence() {
        int count = mContainer.getChildCount();
        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < count; i++) {
            TextView view = (TextView) mContainer.getChildAt(i);
            sentence.append(view.getText() + SPACE);
        }
        return sentence.toString();
    }

    public void addWord(View v) {
        Button button = (Button) v;
        String word = button.getText().toString();

        TextView mTextView= new TextView(mContext);
        mTextView.setText(word);

        mContainer.addView(mTextView);
        usedWords.add(word);
        enableEdit = true;
    }

    public void onClickSave(View v) {
        Log.d(TAG, getSentence());
        if (mImagePath == null)  {
            Toast.makeText(this, R.string.select_image, Toast.LENGTH_SHORT).show();
            return;
        }
        showCategoryDialog();
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

        client.post(RequestUrl.UPLOAD_TO_SERVER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                try {
                    String imageUrl = new String(bytes, "UTF-8");
                    writing.setImageUrl(imageUrl);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException : " + e);
                }
                finish();

                // save data to sqlite
                writeToDb();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void writeToDb() {
        // Get the data repository in write mode
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WritingOpenHelper.KEY_NAME, writing.getName());
        values.put(WritingOpenHelper.KEY_EMAIL, writing.getEmail());
        values.put(WritingOpenHelper.KEY_SENTENCE, writing.getSentence());
        values.put(WritingOpenHelper.KEY_WORDS, writing.getWords());
        values.put(WritingOpenHelper.KEY_IMAGE_URL, writing.getImageUrl());
        values.put(WritingOpenHelper.KEY_CATEGORY, writing.getCategory());
        values.put(WritingOpenHelper.KEY_DATE, writing.getDate());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WritingOpenHelper.WRITING_TABLE_NAME, null, values);
        Log.d(TAG, "" + newRowId);
        db.close();
    }

    private void setWriting(String sentence, String words, String date) {
        writing = new Writing(user.getName(), user.getEmail(), sentence,
                words, "", mCategory, date);
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
                    mCategory = data.getIntExtra("category", Category.NO_CATEOGRY);
                    if(mCategory != Category.NO_CATEOGRY) {
                        String sentence = getSentence();
                        String words = Arrays.toString(usedWords.toArray(new String[usedWords.size()]));
                        String date = String.valueOf(System.currentTimeMillis());

                        // set wrting
                        setWriting(sentence, words, date);

                        // send data to server
                        sendMultipart(sentence, words, date);

                    } else {
                        Toast.makeText(this, R.string.select_category, Toast.LENGTH_SHORT).show();
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

    // EditTextWatcher는 현재 FlowLayout에 있는 텍스트의 전체길이와
    // 추가 된 editText의 길이가 Max length가 넘는지 체크하기 위한 class
    private class EditTextWatcher implements TextWatcher {
        private int sentenceLength;
        EditTextWatcher(int sentenceLength) {
            this.sentenceLength = sentenceLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() + sentenceLength > MAX_SENTENCE_LENGTH) {
                int start = s.length() - 1;
                int end = start + 1;
                s.delete(start, end);
                return;
            }
        }
    }
}