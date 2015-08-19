package com.nhnnext.android.kumdo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhnnext.android.kumdo.fragment.BestFragment;
import com.nhnnext.android.kumdo.fragment.CategoryFragment;
import com.nhnnext.android.kumdo.fragment.MylistFragment;
import com.nhnnext.android.kumdo.model.User;
import com.nhnnext.android.kumdo.util.XmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangyoungbin on 15. 8. 10..
 * ToolBar는 API 21부터 추가되었기 때문에, minSdk가 16이므로,
 * 아랫 버전 호환을 위해 AppCompatActivity로 상속(`15.08.10 by jyb)
 */
public class MenuActivity extends AppCompatActivity {
    public static final String EMPTY_TITLE = "";
    public static final String HOME = "Home";
    public static final String CATEGORY = "Category";
    public static final String MY_LIST = "My List";
    private static final String TAG = "MenuActivity";
    private DrawerLayout mDrawerLayout;

    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;
    private User user;

    private OAuthLogin mOAuthLoginInstance;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mUserImage = (ImageView) findViewById(R.id.user_image);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserEmail = (TextView) findViewById(R.id.user_email);

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mContext = this;

        initActionBar();

        initNavigation();

        initTabView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        RequestApiTask requestApiTask = new RequestApiTask();
        requestApiTask.execute();
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
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_write:
                openWrite();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        adapter.addFragment(new BestFragment(), HOME);
        adapter.addFragment(new CategoryFragment(), CATEGORY);
        adapter.addFragment(new MylistFragment(), MY_LIST);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(adapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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
        private final List<String> mFragmentsTitle = new ArrayList<>();

        public Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
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

        @Override
        public CharSequence getPageTitle(int position) {
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

            BitmapWorkerTask task = new BitmapWorkerTask(mUserImage);
            task.execute(user.getProfile_image());
        }
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private static final String TAG = "BitmapWorkerTask";
        private final WeakReference<ImageView> imageViewReference;
        public String data = "";

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        //TODO Volley로 변경
        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, "doInBackground - starting work");

            data = params[0];

            try {
                HttpURLConnection conn = (HttpURLConnection )new URL(data).openConnection();
                InputStream is = conn.getInputStream();

                return decodeSampledBitmapFromStream(is, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    Log.d(TAG, "onPostExecute - setting bitmap");
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromStream(InputStream is, int inSampleSize) {
            Log.d(TAG, "decodeSampledBitmapFromStream - resizing bitmap");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeStream(is, null, options);
        }
    }
}