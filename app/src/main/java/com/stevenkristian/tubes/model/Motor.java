package com.stevenkristian.tubes.model;


import android.view.View;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;

import java.io.Serializable;

public class Motor  implements Serializable {

    public int id;
    public String merk;
    public String warna;
    public String plat;
    public String tahun;
    public String status;
    public double harga;
    public String imgURL;

    public Motor(int id,String merk, String warna, String plat, String tahun, String status, double harga, String imgURL) {
        this.merk = merk;
        this.warna = warna;
        this.plat = plat;
        this.tahun = tahun;
        this.status = status;
        this.harga = harga;
        this.imgURL = imgURL;
    }
    public Motor(String merk, String warna, String plat, String tahun, String status, double harga, String imgURL) {
        this.merk = merk;
        this.warna = warna;
        this.plat = plat;
        this.tahun = tahun;
        this.status = status;
        this.harga = harga;
        this.imgURL = imgURL;
    }
    public int getIdMotor() {
        return id;
    }

    public void setIdMotor(int id) {
        this.id = id;
    }

    public String getMerk()
    {
        return merk;
    }

    public void setMerk(String merk)
    {
        this.merk = merk;
    }

    public String getWarna()
    {
        return warna;
    }

    public void setWarna(String warna)
    {
        this.warna = warna;
    }

    public String getPlat()
    {
        return plat;
    }

    public void setPlat(String plat)
    {
        this.plat = plat;
    }

    public String getTahun()
    {
        return tahun;
    }

    public void setTahun(String tahun)
    {
        this.tahun = tahun;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public double getHarga()
    {
        return harga;
    }

    public void setHarga(double harga)
    {
        this.harga = harga;
    }

    public String getStringHarga()
    {
        return String.valueOf(harga);
    }

    public String getImgURL() { return imgURL; }

    public void setImgURL(String imgURL)
    {
        this.imgURL = imgURL;
    }

    @BindingAdapter("android:imageURL")
    public static void loadImage(View view, String imageURL)
    {
        ImageView imageView = (ImageView) view;
        if (!imageURL.equals("")){
            Glide.with(view.getContext())
                    .load(MotorAPI.URL_IMAGE + imageURL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
       }else{
            imageView.setImageResource(R.drawable.ic_baseline_broken_image_24);
        }
    }
}

