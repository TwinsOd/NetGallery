package com.example.twins.netgallery.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.twins.netgallery.R;

public class ImageFragment extends Fragment {
    private String comments, url;
    private static final String ARG_IMG_URL = "image_url";
    private static final String ARG_IMG_COMMENTS = "image_comments";

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.comments = args.getString(ARG_IMG_COMMENTS);
        this.url = args.getString(ARG_IMG_URL);
    }

    public static ImageFragment newInstance(String name, String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMG_COMMENTS, name);
        args.putString(ARG_IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.show_image);
        Glide.with(getActivity()).load(url).thumbnail(0.1f).into(imageView);

        return rootView;
    }
}
