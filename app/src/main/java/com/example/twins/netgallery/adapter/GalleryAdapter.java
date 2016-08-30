package com.example.twins.netgallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.twins.netgallery.R;
import com.example.twins.netgallery.model.ImageModel;
import com.example.twins.netgallery.ui.ShowImageActivity;

import java.util.ArrayList;

/**
 * Created by Twins on 24.08.2016.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    Context context;
    ArrayList<ImageModel> data;

    public GalleryAdapter(Activity activity, ArrayList<ImageModel> data) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        viewHolder = new ImageItemHolder(v, activity, data);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Glide.with(context).load(data.get(position).getUrl())
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ImageItemHolder) holder).mImg);

        if (data.get(position).isFavourites())
            ((ImageItemHolder) holder).mIconFavourites.setVisibility(View.VISIBLE);
//            Glide.with(context).load(data.get(position).getUrl())
//                    .thumbnail(0.5f)
//                    .override(24, 24)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(((ImageItemHolder) holder).mIconFavourites);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ImageItemHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        ImageView mIconFavourites;


        public ImageItemHolder(View itemView, final Activity activity, final ArrayList<ImageModel> data) {
            super(itemView);
            mIconFavourites = (ImageView) itemView.findViewById(R.id.icon_fav);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("pos", getPosition());
                    intent.putParcelableArrayListExtra("data", data);
                    activity.startActivityForResult(intent, 0);
                }
            });
        }

    }


}
