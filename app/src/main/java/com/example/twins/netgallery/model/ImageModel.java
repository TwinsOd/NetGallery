package com.example.twins.netgallery.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

/**
 * Created by Twins on 24.08.2016.
 */

public class ImageModel implements Parcelable {

    private String id;
    private int number;
    private String url;
    private boolean favourites;
    private String comments;

    protected ImageModel(Parcel in) {
        id = in.readString();
        number = in.readInt();
        url = in.readString();
        favourites = in.readSparseBooleanArray().get(0);
        comments = in.readString();
    }

    public ImageModel(String id,int number,String url,boolean favourites, String comments){
        this.id = id;
        this.number =number;
        this.url = url;
        this.favourites = favourites;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFavourites() {
        return favourites;
    }

    public void setFavourites(boolean favourites) {
        this.favourites = favourites;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        sparseBooleanArray.put(0,favourites);
        parcel.writeString(id);
        parcel.writeInt(number);
        parcel.writeString(url);
        parcel.writeSparseBooleanArray(sparseBooleanArray);
        parcel.writeString(comments);
    }
    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}
