package com.example.twins.netgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.twins.netgallery.model.ImageModel;
import com.example.twins.netgallery.ui.ImageFragment;

import java.util.ArrayList;

/**
 * Created by Twins on 26.08.2016.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {
    public ArrayList<ImageModel> data = new ArrayList<>();
    private int position;

    public ImagePagerAdapter(FragmentManager fm, ArrayList<ImageModel> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;
        return ImageFragment.newInstance(data.get(position).getComments(), data.get(position).getUrl());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getComments();
    }

    public void setData(ArrayList<ImageModel> data){
        this.data = data;
    }
}
