package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nhnnext.android.kumdo.model.Category;

/**
 * 데이터를 저장하기 전에 카테고리를 선택하기 위한 Activity
 * Dialog 형식처럼 배경은 transparent를 적용.
 * 선택 된 카테고리 번호를 호출한 Activity로 전달한다
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.23 by jyb)
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CategoryActivity";
    private static final String CATEGORY = "category";

    Intent intent;
    private Context mContext;
    private Button mRomanceButton;
    private Button mFamilyButton;
    private Button mFriendButton;
    private Button mAdventureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_dialog);
        intent = new Intent(this, WriteActivity.class);
        mContext = this;

        mRomanceButton = (Button) findViewById(R.id.category_romance);
        mFamilyButton = (Button) findViewById(R.id.category_family);
        mFriendButton = (Button) findViewById(R.id.category_friend);
        mAdventureButton = (Button) findViewById(R.id.category_adventure);

        mRomanceButton.setOnClickListener(this);
        mFamilyButton.setOnClickListener(this);
        mFriendButton.setOnClickListener(this);
        mAdventureButton.setOnClickListener(this);
    }

    public void close(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                Log.e(TAG, v.toString());
                break;
            case R.id.category_romance:
                setResult(RESULT_OK, new Intent().putExtra(CATEGORY, Category.ROMANCE));
                finish();
                break;
            case R.id.category_friend:
                setResult(RESULT_OK, new Intent().putExtra(CATEGORY, Category.FRIEND));
                finish();
                break;
            case R.id.category_family:
                setResult(RESULT_OK, new Intent().putExtra(CATEGORY, Category.FAMILY));
                finish();
                break;
            case R.id.category_adventure:
                setResult(RESULT_OK, new Intent().putExtra(CATEGORY, Category.ADVENTURE));
                finish();
                break;
        }
    }
}
