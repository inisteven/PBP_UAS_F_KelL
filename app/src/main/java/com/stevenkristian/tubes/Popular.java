package com.stevenkristian.tubes;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Popular {

    public String merk;
    public String imgURL;

    public Popular(String merk, String imgURL) {
        this.merk = merk;
        this.imgURL = imgURL;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @BindingAdapter("android:imageURL")
    public static void loadImage(View view, String imageURL)
    {
        ImageView imageView = (ImageView) view;
        if (!imageURL.equals("")){
            Glide.with(view.getContext())
                    .load(imageURL).into(imageView);
        }else{

        }         imageView.setImageResource(R.drawable.ic_baseline_broken_image_24);
    }
}
