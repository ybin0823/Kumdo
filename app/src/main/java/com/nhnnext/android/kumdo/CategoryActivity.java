package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by jangyoungbin on 15. 8. 22..
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CATEGORY_ROMANCE = 1;
    private static final int CATEGORY_ADVENTURE = 2;
    private static final int CATEGORY_FAMILY = 3;
    private static final int CATEGORY_FRIEND = 4;

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
        switch(v.getId()) {
            case R.id.category_romance:
                intent.putExtra("category", Category.ROMANCE);
                setResult(RESULT_OK, new Intent().putExtra("category", CATEGORY_ROMANCE));
                finish();
            break;
            case R.id.category_adventure:
                intent.putExtra("category", Category.ADVENTURE);
                setResult(RESULT_OK, new Intent().putExtra("category", CATEGORY_ADVENTURE));
                finish();
            break;
            case R.id.category_family:
                intent.putExtra("category", Category.FAMILY);
                setResult(RESULT_OK, new Intent().putExtra("category", CATEGORY_FAMILY));
                finish();
                break;
            case R.id.category_friend:
                intent.putExtra("category", Category.FRIEND);
                setResult(RESULT_OK, new Intent().putExtra("category", CATEGORY_FRIEND));
                finish();
            break;

        }
    }

    enum Category {
        ROMANCE, ADVENTURE, FAMILY, FRIEND
    }
}
