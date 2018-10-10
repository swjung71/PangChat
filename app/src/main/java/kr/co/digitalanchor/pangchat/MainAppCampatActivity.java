package kr.co.digitalanchor.pangchat;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import kr.co.digitalanchor.pangchat.frag.SectionsPageAdapter;

public class MainAppCampatActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPageAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    private int[] tabIcons = {R.drawable.member, R.drawable.match, R.drawable.friend};
    private int[] tabIconsselected = {R.drawable.member_selected, R.drawable.match_selected, R.drawable.friend_selected};
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AppBarLayout mAppBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app_compat);

        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mAppBar = (AppBarLayout)findViewById(R.id.appbar);
        showChat();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupTabIcons();

        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;

        int deviceHeight = displayMetrics.heightPixels;

        // 꼭 넣어 주어야 한다. 이렇게 해야 displayMetrics가 세팅이 된다.

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Logger.i("displayMetrics.density : " + displayMetrics.density);

        Logger.i("deviceWidth : " + deviceWidth + ", deviceHeight : " + deviceHeight);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_app_campat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id ==android.R.id.home){
            showChat();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupTabIcons(){
        getSupportActionBar().setElevation(0);
        mTabLayout.getTabAt(1).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(3).setIcon(tabIcons[2]);
    }

    private void showChat(){
        // TODO: 2016. 11. 5. chatting 화면 전환시 에러, mAppBar 사라지는 문제
        mViewPager.setCurrentItem(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mAppBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        //mToolbar.setBackground(getResources().getDrawable(R.drawable.boarder));
        getSupportActionBar().setElevation(0);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mViewPager.getLayoutParams();
        /*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mViewPager.getLayoutParams();
        Display display = getWindowManager().getDefaultDisplay();
        int screenHeight = display.getHeight();

        Rect rc = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rc);
        int statusBarHeight = rc.top;

        int topBarHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

        params.height = screenHeight - topBarHeight;*/
        //mViewPager.setLayoutParams(params);

        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(params);
    }

    private void notShowChat(){
        Logger.i("notShowChat");
        mViewPager.getChildAt(0).clearFocus();
        mAppBar.setBackgroundColor(getResources().getColor(R.color.top_bar));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
        mToolbar.setTitle(getResources().getString(R.string.back));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        //mToolbar.setBackground(getResources().getDrawable(R.drawable.boarder_trans));
        //mAppBar.setBackground(getResources().getDrawable(R.drawable.boarder_trans));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mViewPager.getLayoutParams();
        int height = mAppBar.getHeight();

        Logger.i("measured height : " + height);

        Display display = getWindowManager().getDefaultDisplay();

        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Point size = new Point();
        display.getSize(size);

        Logger.i("window pixel: " + content.getHeight());
        Logger.i("display pixel : " + size.y);

        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        params.height = content.getHeight() - height - statusBarHeight;

        //params.height = dpToPx(params.height);
        mViewPager.setLayoutParams(params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Logger.i("onPageSelected");
        switch (position){
            case 0:
                showChat();
                mTabLayout.getTabAt(1).setIcon(tabIcons[0]);
                mTabLayout.getTabAt(2).setIcon(tabIcons[1]);
                mTabLayout.getTabAt(3).setIcon(tabIcons[2]);
                break;
            case 1:
                notShowChat();
                mTabLayout.getTabAt(1).setIcon(tabIconsselected[0]);
                mTabLayout.getTabAt(2).setIcon(tabIcons[1]);
                mTabLayout.getTabAt(3).setIcon(tabIcons[2]);
                break;
            case 2:
                notShowChat();
                mTabLayout.getTabAt(1).setIcon(tabIcons[0]);
                mTabLayout.getTabAt(2).setIcon(tabIconsselected[1]);
                mTabLayout.getTabAt(3).setIcon(tabIcons[2]);
                break;
            case 3:
                notShowChat();
                mTabLayout.getTabAt(1).setIcon(tabIcons[0]);
                mTabLayout.getTabAt(2).setIcon(tabIcons[1]);
                mTabLayout.getTabAt(3).setIcon(tabIconsselected[2]);
                break;
            default:
                break;
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

/*
        if(mViewPager.getChildAt(mViewPager.getCurrentItem()) instanceof IOnFocusListenable) {
            ((IOnFocusListenable) mViewPager.getChildAt(mViewPager.getCurrentItem()) ).onWindowFocusChanged(hasFocus);
        }*/
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
