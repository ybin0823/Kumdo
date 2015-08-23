package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nhnnext.android.kumdo.fragment.BestFragment;

/**
 * 선택 된 카테고리 번호에 따라 title을 바꾼다
 * 선택 된 카테고리 번호를 BestFragment로 전달하여, 해당 되는 카테고리 정보만 가져온다
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.23 by jyb)
 */
public class DetailCategoryActivity extends AppCompatActivity {
    private int mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSelectedCategory = getIntent().getIntExtra("category", -1);
        actionBar.setTitle(getCategoryName(mSelectedCategory));

    }

    private int getCategoryName(int selectedCategory) {
        switch(selectedCategory) {
            case 0:
                return R.string.category_romance;
            case 1:
                return R.string.category_adventure;
            case 2:
                return R.string.category_family;
            case 3:
                return R.string.category_friend;

        }
        return -1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BestFragment bestFragment = new BestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", mSelectedCategory);
        bestFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.detail_category, bestFragment);
        fragmentTransaction.commit();
    }
}
