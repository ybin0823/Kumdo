package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private static final String TAG = "DetailActivity";
    public static final String WRITING_DATA_EXTRA = "extra_writing";

    private NetworkImageView imageView;
    private TextView sentence;
    private TextView words;
    private TextView name;
    private TextView date;
    private Writing writing;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initActionBar();
        initData();
        initView();
    }

    private void initView() {
        imageView = (NetworkImageView) findViewById(R.id.detail_image);
        sentence = (TextView) findViewById(R.id.sentence);
        words = (TextView) findViewById(R.id.words);
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
    }

    private void initData() {
        mContext = this;
        Intent intent = getIntent();
        writing = intent.getParcelableExtra(WRITING_DATA_EXTRA);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.detail_title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Image, Text 등의 View를 그려준다
        sentence.setText(writing.getSentence());
        words.setText(writing.getWords());
        name.setText(writing.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        // iOS앱의 경우 date가 millisecond가 소수점까지 저장되기 때문에
        // Android에서 dot(.)를 기준으로 split해준다
        date.setText(sdf.format(new Date(Long.valueOf(writing.getDate().split("\\.")[0]))));
        loadImage();
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
     * method : loadImage()
     * url로 서버에 저장된 이미지를 불러와서 ImageView를 그려준다
     */
    public void loadImage() {
        //Image의 width와 height를 통일해서 정사각형 모양으로 만들기 위해 사용
        final RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.detail_container);
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        RelativeLayout.LayoutParams mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                                mLayout.getWidth(), mLayout.getWidth()
                        );
                        imageView.setLayoutParams(mImageViewLayoutParams);
                        imageView.setImageUrl(writing.getImageUrl(), VolleySingleton.getInstance(mContext).getImageLoader());
                    }
                }
        );
    }
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
