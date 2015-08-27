package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhnnext.android.kumdo.fragment.BestFragment;
import com.nhnnext.android.kumdo.fragment.CategoryFragment;
import com.nhnnext.android.kumdo.fragment.MylistFragment;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.util.XmlParser;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangyoungbin on 15. 8. 10..
 * ToolBar는 API 21부터 추가되었기 때문에, minSdk가 16이므로,
 * 아랫 버전 호환을 위해 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";

    public static final String EMPTY_TITLE = "";
    public static final int CategoryTab = 1;

    private DrawerLayout mDrawerLayout;

    private NetworkImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;
    private User user;

    private OAuthLogin mOAuthLoginInstance;
    private Context mContext;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mUserImage = (NetworkImageView) findViewById(R.id.user_image);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserEmail = (TextView) findViewById(R.id.user_email);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mContext = this;

        initActionBar();

        initNavigation();

        initTabView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        RequestApiTask requestApiTask = new RequestApiTask();
        requestApiTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTabView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_36dp).setText(EMPTY_TITLE);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list_white_36dp).setText(EMPTY_TITLE);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_collections_white_36dp).setText(EMPTY_TITLE);
    }

    private void initNavigation() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                Log.e(TAG, item.toString());
                return false;
            case android.R.id.home:
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
            case R.id.action_write:
            openWrite();
            return true;
            case R.id.action_settings:
            openSettings();
            return true;
        }
    }

    private void openWrite() {
        Intent intent = new Intent(this, WriteActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        final Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new BestFragment(), R.string.home);
        adapter.addFragment(new CategoryFragment(), R.string.category);
        adapter.addFragment(new MylistFragment(), R.string.mylist);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(adapter.getTitle(position));
                if (position == CategoryTab) {
                    setAnimationCategoryName();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == CategoryTab) {
                    setAnimationCategoryName();
                    progressBar.setVisibility(View.GONE);
                }
            }

            //TabLayout의 경우 앞, 뒤 Fragment를 같이 호출한다.
            //즉, 사실 상은 가로로 긴 화면인데, 내가 보는 화면만 보여주는 것이다. 그러므로 탭이 바뀌어도 onStart, onResume이 호출되지 않는다.
            //이런 이유로 탭이 체인지 되었을 때 Adapter.getView()에 걸어둔 애니메이션이 동작하지 않는다.
            //따라서 별도로 ListView에서 category_name view에만 animation을 적용해준다
            private void setAnimationCategoryName() {
                ListView container = (ListView) findViewById(R.id.category_container);

                for(int i = 0; i < container.getChildCount(); i++) {
                    ViewGroup viewGroup = (ViewGroup) container.getChildAt(i);
                    for(int j = 0; j < viewGroup.getChildCount(); j++) {
                        View view = viewGroup.getChildAt(j);
                        if(R.id.category_name == view.getId()) {
                            Animation textMoveAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.text_move);
                            view.startAnimation(textMoveAnimation);
                        }
                    }
                }
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<Integer> mFragmentsTitle = new ArrayList<>();

        public Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, int title) {
            mFragments.add(fragment);
            mFragmentsTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public @StringRes int getTitle(int position) {
            return mFragmentsTitle.get(position);
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://apis.naver.com/nidlogin/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }

        protected void onPostExecute(String content) {
            Log.d(TAG, "requestApi : " + content);
            user = XmlParser.parse(content);
            mUserName.setText(user.getName());
            mUserEmail.setText(user.getEmail());

            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences("userInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userEmail", user.getEmail());

            // Commit the edits!
            editor.commit();

            // Set user image
            mUserImage.setImageUrl(user.getProfile_image(),
                    VolleySingleton.getInstance(mContext).getImageLoader());
        }
    }
}