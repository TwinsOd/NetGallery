package com.example.twins.netgallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.twins.netgallery.R;
import com.example.twins.netgallery.adapter.ImagePagerAdapter;
import com.example.twins.netgallery.model.ImageModel;
import com.example.twins.netgallery.transformer.CardTransformer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShowImageActivity extends AppCompatActivity implements View.OnClickListener {
    final String DEFAULT_DURATION_SLIDE_SHOW = "3";
    private int position;
    public ArrayList<ImageModel> mDataList = new ArrayList<>();
    private ViewPager mViewPager;
    private ImagePagerAdapter imagePagerAdapter;
    private Boolean isSlideShow = false;
    private Timer timer;
    private Toolbar mToolbar;
    private SharedPreferences sharedPreferences;
    private Menu menu;
    private RelativeLayout mLayoutWhiteComments;
    private ImageView mImageSendComments;
    private EditText mEditTextComments;
    private TextView mTextViewComments;
    private LinearLayout mLayoutComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_show_image);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mLayoutWhiteComments = (RelativeLayout) findViewById(R.id.layout_white_comments);
        mLayoutWhiteComments.setVisibility(View.GONE);

        mLayoutComments = (LinearLayout) findViewById(R.id.linear_layout_comments);
        mLayoutComments.setVisibility(View.GONE);

        mImageSendComments = (ImageView) findViewById(R.id.image_send_comments);
        mImageSendComments.setOnClickListener(this);

        mEditTextComments = (EditText) findViewById(R.id.edit_text_comments);
        mTextViewComments = (TextView) findViewById(R.id.text_comments);

        mDataList = getIntent().getParcelableArrayListExtra("data");
        position = getIntent().getIntExtra("pos", 0);

        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mDataList);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        chooseTransformer();

        mViewPager.setAdapter(imagePagerAdapter);
        mViewPager.setCurrentItem(position);

        setSlideShow();
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        setComments(position);
    }

    private void setIconSlideShow() {
        if (sharedPreferences.getBoolean("slide_show", false)) {
            isSlideShow = true;
            menu.getItem(1).setIcon(R.drawable.ic_pause_24dp);
        }
    }

    private void chooseTransformer() {
        if (sharedPreferences.getString("type_of_animation", "").equals("appearance")) {
            mViewPager.setPageTransformer(true, new CardTransformer(1f));
        } else if (sharedPreferences.getString("type_of_animation", "").equals("scroll")) {
            mViewPager.setPageTransformer(true, new CardTransformer(0.1f));
        }
    }

    private final ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mLayoutWhiteComments.setVisibility(View.GONE);
            setFavounitesIcon(position);
            setComments(position);
        }
    };

    private void setFavounitesIcon(int position) {
        this.position = position;
        if (mDataList.get(position).isFavourites())
            menu.getItem(0).setIcon(R.drawable.ic_grade_24dp);
        else
            menu.getItem(0).setIcon(R.drawable.ic_star_border_24dp);
    }

    private void setSlideShow() {
        if (sharedPreferences.getBoolean("slide_show", false)) {
            beginSlideShow();
            isSlideShow = true;
        } else timer = null;
    }

    private void setComments(int position) {
        if (mDataList.get(position).getComments().length() > 0) {
            mTextViewComments.setText(mDataList.get(position).getComments());
            mLayoutComments.setVisibility(View.VISIBLE);
        } else mLayoutComments.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_image, menu);
        setFavounitesIcon(0);
        setIconSlideShow();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_favorite:
                if (mDataList.get(position).isFavourites()) {
                    mDataList.get(position).setFavourites(false);
                    item.setIcon(R.drawable.ic_star_border_24dp);
                    mLayoutWhiteComments.setVisibility(View.GONE);
                } else {
                    mDataList.get(position).setFavourites(true);
                    item.setIcon(R.drawable.ic_grade_24dp);
                    mLayoutWhiteComments.setVisibility(View.VISIBLE);
                    mEditTextComments.setFocusableInTouchMode(true);
                    mEditTextComments.requestFocus();
                }
                imagePagerAdapter.setData(mDataList);
                break;
            case R.id.icon_slide_show:
                if (!isSlideShow) {
                    beginSlideShow();
                    isSlideShow = true;
                    item.setIcon(R.drawable.ic_pause_24dp);
                } else {
                    timer.cancel();
                    timer = null;
                    isSlideShow = false;
                    item.setIcon(R.drawable.ic_play_24dp);
                }
                break;
        }
        return true;
    }

    private int getItem(int i) {
        if (mViewPager.getCurrentItem() + 1 == mDataList.size())
            return 0;

        return mViewPager.getCurrentItem() + i;
    }


    private void beginSlideShow() {
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                mViewPager.setCurrentItem(getItem(1), true);
            }
        };

        int period = 1000 * Integer.valueOf(sharedPreferences.getString("duration_slide_show", DEFAULT_DURATION_SLIDE_SHOW));
        int delay = period;

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_send_comments:
                mLayoutWhiteComments.setVisibility(View.GONE);
                if(mEditTextComments.getText().toString().length()>0) {
                    mDataList.get(position).setComments(mEditTextComments.getText().toString());
                    mLayoutComments.setVisibility(View.VISIBLE);
                    mTextViewComments.setText(mEditTextComments.getText().toString());
                    mEditTextComments.getText().clear();
                }
                break;
        }
    }

    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra("data", mDataList);
        setResult(Activity.RESULT_OK, resultIntent);
        super.finish();
    }
}
