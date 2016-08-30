package com.example.twins.netgallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.twins.netgallery.R;
import com.example.twins.netgallery.adapter.GalleryAdapter;
import com.example.twins.netgallery.model.ImageModel;
import com.example.twins.netgallery.model.ListImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private SharedPreferences sharedPreferences;
    private ArrayList<ImageModel> mDataList = new ArrayList<>();
    private Boolean isRamdomList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

        mDataList = getDataList();

        showImageList();
        mAdapter = new GalleryAdapter(MainActivity.this, mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showImageList() {
        if (sharedPreferences.getBoolean("show_in_random_order", false) && !isRamdomList) {
            long seed = System.nanoTime();
            Collections.shuffle(mDataList, new Random(seed));
            isRamdomList = true;
        } else if (!sharedPreferences.getBoolean("show_in_random_order", false) && isRamdomList) {
            Collections.sort(mDataList, new Comparator<ImageModel>() {
                @Override
                public int compare(ImageModel imageModel, ImageModel t1) {
                    return Integer.valueOf(imageModel.getNumber()).compareTo(t1.getNumber());
                }
            });
            isRamdomList = false;
        }
        mAdapter = new GalleryAdapter(MainActivity.this, testFavourites(mDataList));
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<ImageModel> testFavourites(ArrayList<ImageModel> list) {
        if (sharedPreferences.getBoolean("show_only_favourites", false)) {
            ArrayList<ImageModel> listFavourites = new ArrayList<>();

            for (ImageModel m : list) {
                if (m.isFavourites()) listFavourites.add(m);
            }

            if (listFavourites.size() > 0)
                return listFavourites;
            else {
                Toast.makeText(this, "No images favorites", Toast.LENGTH_LONG).show();
                return list;
            }
        }
        return list;
    }

    private ArrayList<ImageModel> getDataList() {
        return ListImage.getListImage(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onPause();
        showImageList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    mDataList = data.getParcelableArrayListExtra("data");
                }
                break;
            }
        }
    }
}