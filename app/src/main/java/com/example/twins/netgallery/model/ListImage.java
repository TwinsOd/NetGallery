package com.example.twins.netgallery.model;

import android.app.Activity;

import com.example.twins.netgallery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Twins on 24.08.2016.
 */

public class ListImage {


    public static ArrayList<ImageModel> getListImage(Activity activity){
         ArrayList<ImageModel> imageModelList;


        InputStream is = activity.getResources().openRawResource(R.raw.json_image);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        JSONObject jsonObject;
        JSONArray array = null;
        try {
            jsonObject = new JSONObject(jsonString);
            array = jsonObject.getJSONArray("ImageURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageModelList = fromJson(array);

        return imageModelList;
    }

    private static ImageModel fromJson(final JSONObject object, int number) {
        final String url = object.optString("url", "");

        return new ImageModel("json" + number , number,url, false, "");
    }

    private static ArrayList<ImageModel> fromJson(final JSONArray array) {
        final ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            try {
                final ImageModel imageModel = fromJson(array.getJSONObject(i), i);
                if (null != imageModel) list.add(imageModel);
            } catch (final JSONException ignored) {
            }
        }
        return list;
    }
}
