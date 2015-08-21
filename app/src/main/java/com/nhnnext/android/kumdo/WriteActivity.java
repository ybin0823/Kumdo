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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    public static final String SERVER_ADDRESS_SAVE = "http://192.168.0.3:3000/save";

    private Context mContext;

    private LinearLayout mContainer;
    private Button mConcreteButton;
    private Button mAbstractButton;
    private Button mNatureButton;
    private ImageView mImageView;

    private User user;
    private Writing writing;
    private Set<String> words;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mContext = this;

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

        TextView mTextView= new TextView(mContext);
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

        writing = new Writing(user.getEmail(),  sentence.toString(),
                Arrays.toString(words.toArray(new String[words.size()])));

        Gson gson = new Gson();
        final String json = gson.toJson(writing);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS_SAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "VolleyError :" + volleyError);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", json);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addTodRequestQueue(stringRequest);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("title", "title1");
        try {
            params.put("image", new File(mImagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(SERVER_ADDRESS_SAVE + "Image", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

        Log.d(TAG, "imagePath : " + mImagePath);
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

            mImagePath = picturePath;

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