package com.hm.org.voicepay.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import retrofit2.http.Url;

/**
 * Created by Venkatesh.Guddanti on 3/1/2018.
 */

public class ImageBinding {

    @BindingAdapter({"android:src"})
    public static void setImageBackground(ImageView imageView, int Url)
    {
        imageView.setBackgroundResource(android.R.mipmap.sym_def_app_icon);
    }
}
