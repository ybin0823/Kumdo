package com.nhnnext.android.kumdo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nhnnext.android.kumdo.fragment.CategoryListFragment;
import com.nhnnext.android.kumdo.model.Category;

/**
 * 선택 된 카테고리 번호에 따라 title을 바꾼다
 * 선택 된 카테고리 번호를 BestFragment로 전달하여, 해당 되는 카테고리 정보만 가져온다
 * MenuActivity가 AppCompatActivity를 상속받아야 한다.
 * Theme로 Theme.AppCompat를 사용해야 하므로, 통일을 위해 전부 AppCompatActivity로 상속(`15.08.23 by jyb)
 */
public class DetailCategoryActivity extends AppCompatActivity {
    private static final String TAG = "DetailCategoryActivity";
    private int mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSelectedCategory = getIntent().getIntExtra("category", -1);
        actionBar.setTitle(getCategoryName(mSelectedCategory));

    }

    private int getCategoryName(int selectedCategory) {
        switch(selectedCategory) {
            default:
                Log.e(TAG, "" + selectedCategory);
                return -1;
            case Category.ROMANCE:
                return R.string.category_romance;
            case Category.FRIEND:
                return R.string.category_friend;
            case Category.FAMILY:
                return R.string.category_family;
            case Category.ADVENTURE:
                return R.string.category_adventure;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryListFragment listFragment = new CategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", mSelectedCategory);
        listFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.detail_category, listFragment);
        fragmentTransaction.commit();
    }
}
